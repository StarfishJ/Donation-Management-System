/**
 * Donor Management Page
 * Manage donor information CRUD operations
 */

import React, { useState, useEffect, useCallback } from 'react';
import { 
  Table, 
  Button, 
  Modal, 
  Form, 
  Input, 
  message, 
  Popconfirm, 
  Space,
  Card
} from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import { Donor } from '../types';
import { donorApi } from '../services/api';
import dayjs from 'dayjs';

const DonorManagement: React.FC = () => {
  const [donors, setDonors] = useState<Donor[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingDonor, setEditingDonor] = useState<Donor | null>(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm();

  const loadDonors = useCallback(async () => {
    try {
      setLoading(true);
      let data: Donor[];
      if (searchText.trim()) {
        data = await donorApi.search(searchText);
      } else {
        data = await donorApi.getAll();
      }
      setDonors(data);
    } catch (error) {
      console.error('Failed to load donors:', error);
      message.error('Failed to load donor information');
    } finally {
      setLoading(false);
    }
  }, [searchText]);

  useEffect(() => {
    loadDonors();
  }, [loadDonors]);

  const handleAdd = () => {
    setEditingDonor(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleEdit = (donor: Donor) => {
    setEditingDonor(donor);
    // 解析 contactInfo 字段到各个子字段
    const contactInfo = donor.contactInfo || '';
    const phoneMatch = contactInfo.match(/Phone:\s*([^\n]+)/);
    const emailMatch = contactInfo.match(/Email:\s*([^\n]+)/);
    const addressMatch = contactInfo.match(/Address:\s*([^\n]+)/);
    
    form.setFieldsValue({
      name: donor.name,
      phone: phoneMatch ? phoneMatch[1].trim() : '',
      email: emailMatch ? emailMatch[1].trim() : '',
      address: addressMatch ? addressMatch[1].trim() : contactInfo
    });
    setModalVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await donorApi.delete(id);
      message.success('Deleted successfully');
      loadDonors();
    } catch (error) {
      console.error('Delete failed:', error);
      message.error('Delete failed');
    }
  };

  const handleSubmit = async (values: any) => {
    try {
      // 合并联系方式字段
      const contactInfo = `Phone: ${values.phone}\nEmail: ${values.email || 'N/A'}\nAddress: ${values.address}`;
      
      const submitData = {
        name: values.name,
        contactInfo: contactInfo
      };
      
      if (editingDonor) {
        await donorApi.update(editingDonor.id!, submitData);
        message.success('Updated successfully');
      } else {
        await donorApi.create(submitData);
        message.success('Added successfully');
      }
      setModalVisible(false);
      loadDonors();
    } catch (error) {
      console.error('Save failed:', error);
      message.error('Save failed');
    }
  };

  const handleSearch = () => {
    loadDonors();
  };

  // 解析联系方式字段
  const parseContactInfo = (contactInfo: string) => {
    const phoneMatch = contactInfo?.match(/Phone:\s*([^\n]+)/);
    const emailMatch = contactInfo?.match(/Email:\s*([^\n]+)/);
    const addressMatch = contactInfo?.match(/Address:\s*([^\n]+)/);
    
    return {
      phone: phoneMatch ? phoneMatch[1].trim() : '',
      email: emailMatch ? emailMatch[1].trim() : '',
      address: addressMatch ? addressMatch[1].trim() : contactInfo || ''
    };
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      width: 220,
      ellipsis: true,
      render: (text: string) => <strong>{text}</strong>,
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
      width: 160,
      render: (_: any, record: Donor) => {
        const { phone } = parseContactInfo(record.contactInfo || '');
        return phone || '-';
      },
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
      width: 220,
      ellipsis: true,
      render: (_: any, record: Donor) => {
        const { email } = parseContactInfo(record.contactInfo || '');
        return email || '-';
      },
    },
    {
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
      width: 320,
      ellipsis: true,
      render: (_: any, record: Donor) => {
        const { address } = parseContactInfo(record.contactInfo || '');
        return address || '-';
      },
    },
    {
      title: 'Registration Date',
      dataIndex: 'createdAt',
      key: 'createdAt',
      align: 'center' as const,
      render: (date: string) => dayjs(date).format('YYYY-MM-DD HH:mm'),
      width: 160,
    },
    {
      title: 'Actions',
      key: 'action',
      width: 140,
      fixed: 'right' as const,
      render: (_: any, record: Donor) => (
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
            title="Are you sure you want to delete this donor?"
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
            <h2 style={{ margin: 0 }}>Donor Management</h2>
            <p style={{ margin: '4px 0 0 0', color: '#666' }}>
              Manage shelter donor information
            </p>
          </div>
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={handleAdd}
          >
            Add Donor
          </Button>
        </div>

        <div style={{ marginBottom: '16px', display: 'flex', gap: '8px' }}>
          <Input
            placeholder="Search donor name"
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 200 }}
          />
          <Button 
            icon={<SearchOutlined />}
            onClick={handleSearch}
          >
            Search
          </Button>
        </div>

        <Table
          columns={columns}
          dataSource={donors}
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
        title={editingDonor ? 'Edit Donor' : 'Add Donor'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >
          <Form.Item
            name="name"
            label="Name"
            rules={[
              { required: true, message: 'Please enter name' },
              { min: 2, message: 'Name must be at least 2 characters' },
              { max: 100, message: 'Name cannot exceed 100 characters' },
              { pattern: /^[a-zA-Z\s\u4e00-\u9fff]+$/, message: 'Name can only contain letters and spaces' }
            ]}
          >
            <Input placeholder="Please enter donor name" />
          </Form.Item>

          <Form.Item
            name="phone"
            label="Phone Number"
            rules={[
              { required: true, message: 'Please enter phone number' },
              { pattern: /^[+]?[1-9][\d]{0,15}$/, message: 'Please enter a valid phone number' }
            ]}
          >
            <Input placeholder="Please enter phone number" />
          </Form.Item>

          <Form.Item
            name="email"
            label="Email Address"
            rules={[
              { type: 'email', message: 'Please enter a valid email address' }
            ]}
          >
            <Input placeholder="Please enter email address (optional)" />
          </Form.Item>

          <Form.Item
            name="address"
            label="Address"
            rules={[
              { required: true, message: 'Please enter address' },
              { min: 5, message: 'Address must be at least 5 characters' },
              { max: 200, message: 'Address cannot exceed 200 characters' }
            ]}
          >
            <Input.TextArea 
              placeholder="Please enter address"
              rows={2}
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default DonorManagement;
