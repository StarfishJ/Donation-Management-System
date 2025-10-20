/**
 * Reports Page
 * Display inventory reports and donor reports
 */

import React, { useState, useEffect } from 'react';
import { 
  Card, 
  Tabs, 
  Table, 
  Statistic, 
  Row, 
  Col, 
  Spin, 
  Alert,
  Tag,
  Button
} from 'antd';
import { 
  BarChartOutlined, 
  UserOutlined, 
  GiftOutlined,
  ReloadOutlined
} from '@ant-design/icons';
import { InventoryReport, DonorReport, SystemStatistics } from '../types';
import { reportApi } from '../services/api';
import dayjs from 'dayjs';

const Reports: React.FC = () => {
  const [inventoryReports, setInventoryReports] = useState<InventoryReport[]>([]);
  const [donorReports, setDonorReports] = useState<DonorReport[]>([]);
  const [statistics, setStatistics] = useState<SystemStatistics | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadReports();
  }, []);

  const loadReports = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const [inventoryData, donorData, statsData] = await Promise.all([
        reportApi.getInventoryReport(),
        reportApi.getDonorReport(),
        reportApi.getStatistics()
      ]);
      
      setInventoryReports(inventoryData);
      setDonorReports(donorData);
      setStatistics(statsData);
    } catch (err) {
      console.error('Failed to load reports:', err);
      setError('Failed to load reports, please try again later');
    } finally {
      setLoading(false);
    }
  };

  const inventoryColumns = [
    {
      title: 'Donation Type',
      dataIndex: 'donationType',
      key: 'donationType',
      render: (type: string) => <Tag color="blue">{type}</Tag>,
    },
    {
      title: 'Total Received',
      dataIndex: 'totalReceived',
      key: 'totalReceived',
      render: (quantity: number, record: InventoryReport) => (
        <Statistic
          value={quantity}
          suffix={record.unit}
          valueStyle={{ color: '#3f8600', fontSize: '14px' }}
        />
      ),
    },
    {
      title: 'Total Distributed',
      dataIndex: 'totalDistributed',
      key: 'totalDistributed',
      render: (quantity: number, record: InventoryReport) => (
        <Statistic
          value={quantity}
          suffix={record.unit}
          valueStyle={{ color: '#1890ff', fontSize: '14px' }}
        />
      ),
    },
    {
      title: 'Remaining Quantity',
      dataIndex: 'remainingQuantity',
      key: 'remainingQuantity',
      render: (quantity: number, record: InventoryReport) => (
        <Statistic
          value={quantity}
          suffix={record.unit}
          valueStyle={{ 
            color: quantity > 0 ? '#cf1322' : '#999',
            fontSize: '14px'
          }}
        />
      ),
    },
    {
      title: 'Distribution Rate',
      key: 'distributionRate',
      render: (_: any, record: InventoryReport) => {
        const rate = record.totalReceived > 0 
          ? (record.totalDistributed / record.totalReceived * 100).toFixed(1)
          : '0';
        return (
          <Statistic
            value={rate}
            suffix="%"
            valueStyle={{ 
              color: parseFloat(rate) > 80 ? '#3f8600' : '#faad14',
              fontSize: '14px'
            }}
          />
        );
      },
    },
  ];

  const donorColumns = [
    {
      title: 'Donor',
      dataIndex: 'donorName',
      key: 'donorName',
      render: (name: string) => <strong>{name}</strong>,
    },
    {
      title: 'Contact Info',
      dataIndex: 'contactInfo',
      key: 'contactInfo',
      ellipsis: true,
      render: (text: string) => text || '-',
    },
    {
      title: 'First Donation',
      dataIndex: 'firstDonationDate',
      key: 'firstDonationDate',
      render: (date: string) => dayjs(date).format('YYYY-MM-DD'),
      width: 120,
    },
    {
      title: 'Last Donation',
      dataIndex: 'lastDonationDate',
      key: 'lastDonationDate',
      render: (date: string) => dayjs(date).format('YYYY-MM-DD'),
      width: 120,
    },
    {
      title: 'Donation Count',
      dataIndex: 'totalDonations',
      key: 'totalDonations',
      render: (count: number) => (
        <Statistic
          value={count}
          suffix="times"
          valueStyle={{ color: '#1890ff', fontSize: '14px' }}
        />
      ),
      width: 100,
    },
    {
      title: 'Total Donation Value',
      dataIndex: 'totalValue',
      key: 'totalValue',
      render: (value: number) => (
        <Statistic
          value={value}
          suffix="dollars"
          precision={2}
          valueStyle={{ color: '#3f8600', fontSize: '14px' }}
        />
      ),
      width: 120,
    },
  ];

  const tabItems = [
    {
      key: 'inventory',
      label: (
        <span>
          <GiftOutlined />
          Inventory Report
        </span>
      ),
      children: (
        <div>
          <div style={{ marginBottom: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3>Inventory Report</h3>
            <Button 
              icon={<ReloadOutlined />}
              onClick={loadReports}
              loading={loading}
            >
              Refresh
            </Button>
          </div>
          
          <Table
            columns={inventoryColumns}
            dataSource={inventoryReports}
            rowKey="donationType"
            loading={loading}
            pagination={false}
            summary={(pageData) => {
              const totalReceived = pageData.reduce((sum, item) => sum + item.totalReceived, 0);
              const totalDistributed = pageData.reduce((sum, item) => sum + item.totalDistributed, 0);
              const totalRemaining = pageData.reduce((sum, item) => sum + item.remainingQuantity, 0);
              
              return (
                <Table.Summary.Row>
                  <Table.Summary.Cell index={0}>
                    <strong>Total</strong>
                  </Table.Summary.Cell>
                  <Table.Summary.Cell index={1}>
                    <Statistic
                      value={totalReceived}
                      valueStyle={{ color: '#3f8600', fontSize: '14px' }}
                    />
                  </Table.Summary.Cell>
                  <Table.Summary.Cell index={2}>
                    <Statistic
                      value={totalDistributed}
                      valueStyle={{ color: '#1890ff', fontSize: '14px' }}
                    />
                  </Table.Summary.Cell>
                  <Table.Summary.Cell index={3}>
                    <Statistic
                      value={totalRemaining}
                      valueStyle={{ color: '#cf1322', fontSize: '14px' }}
                    />
                  </Table.Summary.Cell>
                  <Table.Summary.Cell index={4}>
                    <Statistic
                      value={totalReceived > 0 ? (totalDistributed / totalReceived * 100).toFixed(1) : '0'}
                      suffix="%"
                      valueStyle={{ color: '#faad14', fontSize: '14px' }}
                    />
                  </Table.Summary.Cell>
                </Table.Summary.Row>
              );
            }}
          />
        </div>
      ),
    },
    {
      key: 'donors',
      label: (
        <span>
          <UserOutlined />
          Donor Report
        </span>
      ),
      children: (
        <div>
          <div style={{ marginBottom: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3>Donor Report</h3>
            <Button 
              icon={<ReloadOutlined />}
              onClick={loadReports}
              loading={loading}
            >
              Refresh
            </Button>
          </div>
          
          <Table
            columns={donorColumns}
            dataSource={donorReports}
            rowKey="donorId"
            loading={loading}
            pagination={{
              pageSize: 10,
              showSizeChanger: true,
              showQuickJumper: true,
              showTotal: (total) => `Total ${total} donors`,
            }}
          />
        </div>
      ),
    },
  ];

  if (loading && !inventoryReports.length && !donorReports.length) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
        <div style={{ marginTop: '16px' }}>Loading reports...</div>
      </div>
    );
  }

  if (error) {
    return (
      <Alert
        message="Error"
        description={error}
        type="error"
        showIcon
        action={
          <Button onClick={loadReports}>
            Retry
          </Button>
        }
      />
    );
  }

  return (
    <div>
      <Card style={{ marginBottom: '24px' }}>
        <h2 style={{ marginBottom: '16px' }}>
          <BarChartOutlined /> Statistical Reports
        </h2>
        
        {statistics && (
          <Row gutter={[16, 16]}>
            <Col xs={24} sm={12} lg={6}>
              <Statistic
                title="Total Donors"
                value={statistics.totalDonors}
                prefix={<UserOutlined />}
                valueStyle={{ color: '#3f8600' }}
              />
            </Col>
            <Col xs={24} sm={12} lg={6}>
              <Statistic
                title="Total Donations"
                value={statistics.totalDonations}
                prefix={<GiftOutlined />}
                valueStyle={{ color: '#1890ff' }}
              />
            </Col>
            <Col xs={24} sm={12} lg={6}>
              <Statistic
                title="Total Distributions"
                value={statistics.totalDistributions}
                prefix={<BarChartOutlined />}
                valueStyle={{ color: '#722ed1' }}
              />
            </Col>
            <Col xs={24} sm={12} lg={6}>
              <Statistic
                title="Total Donation Value"
                value={statistics.totalDonationValue}
                suffix="dollars"
                precision={2}
                prefix={<GiftOutlined />}
                valueStyle={{ color: '#cf1322' }}
              />
            </Col>
          </Row>
        )}
      </Card>

      <Card>
        <Tabs 
          defaultActiveKey="inventory"
          items={tabItems}
          size="large"
        />
      </Card>
    </div>
  );
};

export default Reports;
