/**
 * Distribution Management Page
 * Manage distribution records of donated items
 */

import React, { useState, useEffect } from 'react';
import { 
  Table, 
  Button, 
  Modal, 
  Form, 
  Input, 
  InputNumber,
  Select,
  DatePicker,
  message, 
  Popconfirm, 
  Space,
  Card,
  Tag,
  Alert
} from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { Distribution, Donation } from '../types';
import { distributionApi, donationApi } from '../services/api';
import dayjs from 'dayjs';

const DistributionManagement: React.FC = () => {
  const [distributions, setDistributions] = useState<Distribution[]>([]);
  const [donations, setDonations] = useState<Donation[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingDistribution, setEditingDistribution] = useState<Distribution | null>(null);
  const [form] = Form.useForm();
  const [remaining, setRemaining] = useState<number | null>(null);

  useEffect(() => {
    loadDistributions();
    loadDonations();
  }, []);

  const loadDistributions = async () => {
    try {
      setLoading(true);
      const data = await distributionApi.getAll();
      setDistributions(data);
    } catch (error) {
      console.error('Failed to load distributions:', error);
      message.error('Failed to load distributions');
    } finally {
      setLoading(false);
    }
  };

  const loadDonations = async () => {
    try {
      const data = await donationApi.getAll();
      setDonations(data);
    } catch (error) {
      console.error('Failed to load donations:', error);
    }
  };

  const handleAdd = () => {
    setEditingDistribution(null);
    form.resetFields();
    setRemaining(null);
    setModalVisible(true);
  };

  const handleEdit = (distribution: Distribution) => {
    setEditingDistribution(distribution);
    // 解析 recipientInfo 字段到各个子字段
    const recipientInfo = distribution.recipientInfo || '';
    const nameMatch = recipientInfo.match(/Name:\s*([^\n]+)/);
    const contactMatch = recipientInfo.match(/Contact:\s*([^\n]+)/);
    
    form.setFieldsValue({
      donationId: distribution.donationId,
      quantityDistributed: distribution.quantityDistributed,
      recipientName: nameMatch ? nameMatch[1].trim() : '',
      recipientContact: contactMatch ? contactMatch[1].trim() : recipientInfo,
      distributionDate: distribution.distributionDate ? dayjs(distribution.distributionDate) : dayjs(),
      notes: distribution.notes
    });
    setModalVisible(true);
    // load remaining for the selected donation
    if (distribution.donationId) {
      distributionApi.getRemaining(distribution.donationId).then(setRemaining).catch(() => setRemaining(null));
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await distributionApi.delete(id);
      message.success('Deleted successfully');
      loadDistributions();
    } catch (error) {
      console.error('Delete failed:', error);
      message.error('Delete failed');
    }
  };

  const handleSubmit = async (values: any) => {
    try {
      if (remaining != null && values.quantityDistributed > remaining) {
        message.error(`Distributed quantity exceeds remaining (${remaining}).`);
        return;
      }
      // 直接发送分离的字段，不合并
      const submitData = {
        donationId: values.donationId,
        quantityDistributed: values.quantityDistributed,
        recipientName: values.recipientName,
        recipientContact: values.recipientContact,
        distributionDate: values.distributionDate ? values.distributionDate.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : undefined,
        notes: values.notes
      };
      
      console.log('Submitting data:', submitData); // 调试日志
      
      if (editingDistribution) {
        await distributionApi.update(editingDistribution.id!, submitData);
        message.success('Updated successfully');
      } else {
        await distributionApi.create(submitData);
        message.success('Added successfully');
      }
      setModalVisible(false);
      setRemaining(null);
      loadDistributions();
    } catch (error: any) {
      console.error('Save failed:', error);
      if (error.response?.data?.message) {
        message.error(error.response.data.message);
      } else {
        message.error('Save failed');
      }
    }
  };

  const handleDonationChange = async (donationId: number) => {
    try {
      const left = await distributionApi.getRemaining(donationId);
      setRemaining(left);
    } catch {
      setRemaining(null);
    }
  };

  const getDonationInfo = (donationId: number) => {
    return donations.find(d => d.id === donationId);
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: 'Donation Record',
      dataIndex: 'donationId',
      key: 'donationId',
      width: 200,
      render: (donationId: number) => {
        const donation = getDonationInfo(donationId);
        return donation ? (
          <div>
            <div><strong>{donation.donorName}</strong></div>
            <div><Tag color="blue">{donation.donationType}</Tag></div>
          </div>
        ) : `ID: ${donationId}`;
      },
    },
    {
      title: 'Distribution Quantity',
      dataIndex: 'quantityDistributed',
      key: 'quantityDistributed',
      width: 140,
      align: 'right' as const,
      render: (quantity: number, record: Distribution) => {
        const donation = getDonationInfo(record.donationId);
        return (
          <span>
            <strong>{quantity}</strong> {donation?.unit || ''}
          </span>
        );
      },
    },
    {
      title: 'Recipient Name',
      dataIndex: 'recipientName',
      key: 'recipientName',
      width: 200,
      render: (_: any, record: Distribution) => {
        const recipientInfo = record.recipientInfo || '';
        const nameMatch = recipientInfo.match(/Name:\s*([^\n]+)/);
        return nameMatch ? nameMatch[1].trim() : '-';
      },
    },
    {
      title: 'Recipient Contact',
      dataIndex: 'recipientContact',
      key: 'recipientContact',
      width: 260,
      ellipsis: true,
      render: (_: any, record: Distribution) => {
        const recipientInfo = record.recipientInfo || '';
        const contactMatch = recipientInfo.match(/Contact:\s*([^\n]+)/);
        return contactMatch ? contactMatch[1].trim() : '-';
      },
    },
    {
      title: 'Distribution Date',
      dataIndex: 'distributionDate',
      key: 'distributionDate',
      width: 170,
      align: 'center' as const,
      render: (date: string) => dayjs(date).format('YYYY-MM-DD HH:mm'),
    },
    {
      title: 'Notes',
      dataIndex: 'notes',
      key: 'notes',
      width: 260,
      ellipsis: true,
      render: (text: string) => text || '-',
    },
    {
      title: 'Actions',
      key: 'action',
      width: 140,
      fixed: 'right' as const,
      render: (_: any, record: Distribution) => (
        <Space size="small">
          <Button 
            type="primary" 
            size="small" 
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            Edit
          </Button>
          <Popconfirm
            title="Are you sure you want to delete this distribution record?"
            onConfirm={() => handleDelete(record.id!)}
            okText="Confirm"
            cancelText="Cancel"
          >
            <Button 
              type="primary" 
              danger 
              size="small" 
              icon={<DeleteOutlined />}
            >
              Delete
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card>
        <div style={{ marginBottom: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <h2 style={{ margin: 0 }}>Distribution Management</h2>
            <p style={{ margin: '4px 0 0 0', color: '#666' }}>
              Manage distribution records of donated items
            </p>
          </div>
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={handleAdd}
          >
            Add Distribution Record
          </Button>
        </div>

        <Alert
          message="Distribution Instructions"
          description="Distribution quantity cannot exceed the remaining quantity of the corresponding donation record. The system will automatically validate and show errors."
          type="info"
          showIcon
          style={{ marginBottom: '16px' }}
        />

        <Table
          columns={columns}
          dataSource={distributions}
          rowKey="id"
          loading={loading}
          size="large"
          bordered
          sticky
          scroll={{ x: 'max-content', y: 480 }}
          pagination={{
            defaultPageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total) => `Total ${total} records`,
            position: ['bottomRight']
          }}
        />
      </Card>

      <Modal
        title={editingDistribution ? 'Edit Distribution Record' : 'Add Distribution Record'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        destroyOnClose
        width={600}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >
          <Form.Item
            name="donationId"
            label="Select Donation Record"
            rules={[{ required: true, message: 'Please select a donation record' }]}
          >
            <Select 
              placeholder="Please select a donation record to distribute"
              showSearch
              optionFilterProp="children"
              filterOption={(input, option) =>
                (option?.children as unknown as string)?.toLowerCase().includes(input.toLowerCase())
              }
              onChange={handleDonationChange}
            >
              {donations.map(donation => (
                <Select.Option key={donation.id} value={donation.id}>
                  {donation.donorName} - {donation.donationType} ({donation.quantity} {donation.unit})
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          {remaining != null && (
            <div style={{ marginTop: -8, marginBottom: 8, color: '#666' }}>
              Remaining: <strong>{remaining}</strong>
            </div>
          )}

          <Form.Item
            name="quantityDistributed"
            label="Distribution Quantity"
            rules={[
              { required: true, message: 'Please enter distribution quantity' },
              { type: 'number', min: 0.01, message: 'Distribution quantity must be greater than 0' },
              { type: 'number', max: 999999.99, message: 'Distribution quantity cannot exceed 999,999.99' }
            ]}
          >
            <InputNumber 
              placeholder="Please enter distribution quantity"
              style={{ width: '100%' }}
              precision={2}
              min={0.01}
              max={remaining != null ? Math.max(remaining, 0.01) : 999999.99}
            />
          </Form.Item>

          <Form.Item
            name="recipientName"
            label="Recipient Name"
            rules={[
              { required: true, message: 'Please enter recipient name' },
              { min: 2, message: 'Recipient name must be at least 2 characters' },
              { max: 100, message: 'Recipient name cannot exceed 100 characters' }
            ]}
          >
            <Input placeholder="Please enter recipient name" />
          </Form.Item>

          <Form.Item
            name="recipientContact"
            label="Recipient Contact"
            rules={[
              { required: true, message: 'Please enter recipient contact information' },
              { min: 5, message: 'Contact information must be at least 5 characters' },
              { max: 200, message: 'Contact information cannot exceed 200 characters' }
            ]}
          >
            <Input.TextArea 
              placeholder="Please enter recipient contact information (phone, email, etc.)"
              rows={2}
            />
          </Form.Item>

          <Form.Item
            name="distributionDate"
            label="Distribution Date"
            rules={[{ required: true, message: 'Please select distribution date' }]}
            initialValue={dayjs()}
          >
            <DatePicker 
              style={{ width: '100%' }}
              format="YYYY-MM-DD"
              placeholder="Please select distribution date"
            />
          </Form.Item>

          <Form.Item
            name="notes"
            label="Notes"
            rules={[
              { max: 500, message: 'Notes cannot exceed 500 characters' }
            ]}
          >
            <Input.TextArea 
              placeholder="Please enter notes (optional)"
              rows={3}
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default DistributionManagement;
