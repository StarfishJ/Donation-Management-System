/**
 * Donation Management Page
 * Manage donation records CRUD operations
 */

import React, { useState, useEffect } from 'react';
import { 
  Table, 
  Button, 
  Modal, 
  Form, 
  Input, 
  Select, 
  InputNumber,
  DatePicker,
  message, 
  Popconfirm, 
  Space,
  Card,
  Tag
} from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { Donation, Donor, DONATION_TYPES, UNITS } from '../types';
import { donationApi, donorApi } from '../services/api';
import dayjs from 'dayjs';

const DonationManagement: React.FC = () => {
  const [donations, setDonations] = useState<Donation[]>([]);
  const [donors, setDonors] = useState<Donor[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingDonation, setEditingDonation] = useState<Donation | null>(null);
  const [form] = Form.useForm();

  useEffect(() => {
    loadDonations();
    loadDonors();
  }, []);

  const loadDonations = async () => {
    try {
      setLoading(true);
      const data = await donationApi.getAll();
      setDonations(data);
    } catch (error) {
      console.error('Failed to load donations:', error);
      message.error('Failed to load donations');
    } finally {
      setLoading(false);
    }
  };

  const loadDonors = async () => {
    try {
      const data = await donorApi.getAll();
      setDonors(data);
    } catch (error) {
      console.error('Failed to load donors:', error);
    }
  };

  const handleAdd = () => {
    setEditingDonation(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleEdit = (donation: Donation) => {
    setEditingDonation(donation);
    // 只设置表单需要的字段，排除只读字段
    form.setFieldsValue({
      donorId: donation.donorId,
      donationType: donation.donationType,
      quantity: donation.quantity,
      unit: donation.unit,
      donationDate: donation.donationDate ? dayjs(donation.donationDate) : dayjs(),
      description: donation.description,
      notes: donation.notes
    });
    setModalVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await donationApi.delete(id);
      message.success('Deleted successfully');
      loadDonations();
    } catch (error) {
      console.error('Delete failed:', error);
      message.error('Delete failed');
    }
  };

  const handleSubmit = async (values: any) => {
    try {
      const submitData = {
        ...values,
        donationDate: values.donationDate ? values.donationDate.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : undefined
      };
      
      console.log('Submitting data:', submitData); 
      
      if (editingDonation) {
        await donationApi.update(editingDonation.id!, submitData);
        message.success('Updated successfully');
      } else {
        await donationApi.create(submitData);
        message.success('Added successfully');
      }
      setModalVisible(false);
      loadDonations();
    } catch (error) {
      console.error('Save failed:', error);
      message.error('Save failed');
    }
  };

  const getDonationTypeLabel = (type: string) => {
    const typeObj = DONATION_TYPES.find(t => t.value === type);
    return typeObj ? typeObj.label : type;
  };

  const getUnitLabel = (unit: string) => {
    const unitObj = UNITS.find(u => u.value === unit);
    return unitObj ? unitObj.label : unit;
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: 'Donor',
      dataIndex: 'donorName',
      key: 'donorName',
      width: 200,
      ellipsis: true,
      render: (text: string) => <strong>{text}</strong>,
    },
    {
      title: 'Donation Type',
      dataIndex: 'donationType',
      key: 'donationType',
      width: 120,
      render: (type: string) => (
        <Tag color="blue">{getDonationTypeLabel(type)}</Tag>
      ),
    },
    {
      title: 'Quantity/Amount',
      dataIndex: 'quantity',
      key: 'quantity',
      width: 140,
      align: 'right' as const,
      render: (quantity: number, record: Donation) => (
        <span>
          <strong>{quantity}</strong> {getUnitLabel(record.unit)}
        </span>
      ),
    },
    {
      title: 'Unit',
      dataIndex: 'unit',
      key: 'unit',
      width: 100,
      render: (unit: string) => getUnitLabel(unit),
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      width: 320,
      ellipsis: true,
      render: (text: string) => text || '-',
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
      title: 'Donation Date',
      dataIndex: 'donationDate',
      key: 'donationDate',
      width: 170,
      align: 'center' as const,
      render: (date: string) => dayjs(date).format('YYYY-MM-DD HH:mm'),
    },
    {
      title: 'Actions',
      key: 'action',
      width: 140,
      fixed: 'right' as const,
      render: (_: any, record: Donation) => (
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
            title="Are you sure you want to delete this donation record?"
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
            <h2 style={{ margin: 0 }}>Donation Management</h2>
            <p style={{ margin: '4px 0 0 0', color: '#666' }}>
              Manage donation records received by the shelter
            </p>
          </div>
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={handleAdd}
          >
            Add Donation Record
          </Button>
        </div>

        <Table
          columns={columns}
          dataSource={donations}
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
        title={editingDonation ? 'Edit Donation Record' : 'Add Donation Record'}
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
            name="donorId"
            label="Donor"
            rules={[{ required: true, message: 'Please select a donor' }]}
          >
            <Select 
              placeholder="Please select a donor"
              showSearch
              optionFilterProp="children"
              filterOption={(input, option) =>
                (option?.children as unknown as string)?.toLowerCase().includes(input.toLowerCase())
              }
            >
              {donors.map(donor => (
                <Select.Option key={donor.id} value={donor.id}>
                  {donor.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            name="donationType"
            label="Donation Type"
            rules={[{ required: true, message: 'Please select donation type' }]}
          >
            <Select placeholder="Please select donation type">
              {DONATION_TYPES.map(type => (
                <Select.Option key={type.value} value={type.value}>
                  {type.label}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            name="quantity"
            label="Quantity/Amount"
            rules={[
              { required: true, message: 'Please enter quantity or amount' },
              { type: 'number', min: 0.01, message: 'Quantity must be greater than 0' },
              { type: 'number', max: 999999.99, message: 'Quantity cannot exceed 999,999.99' }
            ]}
          >
            <InputNumber 
              placeholder="Please enter quantity or amount"
              style={{ width: '100%' }}
              precision={2}
              min={0.01}
              max={999999.99}
            />
          </Form.Item>

          <Form.Item
            name="unit"
            label="Unit"
            rules={[{ required: true, message: 'Please select unit' }]}
          >
            <Select placeholder="Please select unit">
              {UNITS.map(unit => (
                <Select.Option key={unit.value} value={unit.value}>
                  {unit.label}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            name="donationDate"
            label="Donation Date"
            rules={[{ required: true, message: 'Please select donation date' }]}
            initialValue={dayjs()}
          >
            <DatePicker 
              style={{ width: '100%' }}
              format="YYYY-MM-DD"
              placeholder="Please select donation date"
            />
          </Form.Item>

          <Form.Item
            name="description"
            label="Description"
            rules={[
              { required: true, message: 'Please enter description' },
              { max: 500, message: 'Description cannot exceed 500 characters' }
            ]}
          >
            <Input.TextArea 
              placeholder="Please enter detailed description"
              rows={3}
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
              placeholder="Please enter additional notes (optional)"
              rows={3}
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default DonationManagement;
