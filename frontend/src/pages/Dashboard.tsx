/**
 * Dashboard Page
 * Displays system overview and statistics
 */

import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Statistic, Spin, Alert, Button, Space, Tag, Segmented, Table } from 'antd';
import { 
  UserOutlined, 
  GiftOutlined, 
  ShareAltOutlined, 
  DollarOutlined 
} from '@ant-design/icons';
import { Column, Pie, Line } from '@ant-design/charts';
import { SystemStatistics, InventoryReport, DonorReport } from '../types';
import { useNavigate } from 'react-router-dom';
import { reportApi } from '../services/api';

const Dashboard: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [statistics, setStatistics] = useState<SystemStatistics | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [backendOk, setBackendOk] = useState<boolean>(false);
  const [dbOk, setDbOk] = useState<boolean>(false);
  const [recentDays, setRecentDays] = useState<number>(30);
  const [inventoryReport, setInventoryReport] = useState<InventoryReport[]>([]);
  const [donorReport, setDonorReport] = useState<DonorReport[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    loadStatistics(recentDays);
  }, [recentDays]);

  const loadStatistics = async (days?: number) => {
    try {
      setLoading(true);
      setError(null);
      const [stats, inv, donors] = await Promise.all([
        reportApi.getStatistics(days),
        reportApi.getInventoryReport(),
        reportApi.getDonorReport(),
      ]);
      setStatistics(stats);
      setInventoryReport(inv || []);
      setDonorReport(donors || []);
      setBackendOk(true);
      // If statistics loads, DB is reachable (queries executed)
      setDbOk(true);
    } catch (err) {
      console.error('Failed to load statistics:', err);
      setError('Failed to load statistics, please try again later');
      setBackendOk(false);
      setDbOk(false);
    } finally {
      setLoading(false);
    }
  };

  // Stable color mappings for categories
  const donationTypeColors: Record<string, string> = {
    money: '#5B8FF9',
    food: '#5AD8A6',
    clothing: '#9270CA',
    medicine: '#F6BD16',
    furniture: '#E8684A',
    books: '#6DC8EC',
    toys: '#FF9D4D',
  };

  const donorDistributionColors: Record<string, string> = {
    'Active Donors': '#5B8FF9',
    'New Donors': '#5AD8A6',
  };

  const allocationColors: Record<string, string> = {
    Allocated: '#5B8FF9',
    Remaining: '#E8EDF3',
  };

  // 准备图表数据
  const getDonationTypeData = () => {
    if (!statistics?.donationTypeStats) return [];
    return statistics.donationTypeStats.map((item: any) => ({
      type: item.type,
      value: item.count,
      amount: item.totalAmount || 0
    }));
  };

  const getMonthlyTrendData = () => {
    if (!statistics?.monthlyTrends) return [];
    return statistics.monthlyTrends;
  };

  const getDonorDistributionData = () => {
    if (!statistics?.donorStats) return [];
    return [
      { type: 'Active Donors', value: statistics.donorStats.activeCount },
      { type: 'New Donors', value: statistics.donorStats.newCount },
    ];
  };

  const getAllocationData = () => {
    if (!statistics) return [] as Array<{ type: string; value: number }>;
    const total = statistics.totalDonationValue || 0;
    const allocated = Math.min(statistics.totalDistributedValue || 0, total);
    const remaining = Math.max(total - allocated, 0);
    // 若 total 为 0，返回空让占位提示
    if (total <= 0) return [];
    return [
      { type: 'Allocated', value: allocated },
      { type: 'Remaining', value: remaining },
    ];
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
        <div style={{ marginTop: '16px' }}>Loading...</div>
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
          <button 
            onClick={() => loadStatistics(recentDays)}
            style={{ 
              background: 'none', 
              border: 'none', 
              color: '#1890ff', 
              cursor: 'pointer' 
            }}
          >
            Retry
          </button>
        }
      />
    );
  }

  return (
    <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 12px' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', margin: '8px 0 16px' }}>
        <h1 style={{ margin: 0 }}>System Overview</h1>
        <Segmented
          options={[{ label: '7d', value: 7 }, { label: '30d', value: 30 }, { label: '90d', value: 90 }]}
          value={recentDays}
          onChange={(v) => setRecentDays(v as number)}
        />
      </div>
      
      {/* User Guide on top */}
      <Card 
        title="User Guide" 
        style={{ marginBottom: '16px' }}
        size="small"
        bodyStyle={{ padding: 12 }}
      >
        <div style={{ padding: '8px 0' }}>
          <h4 style={{ margin: '0 0 8px 0' }}>Welcome to the Donation Management System!</h4>
          <ul style={{ margin: 0, paddingLeft: 18 }}>
            <li><strong>Donor Management</strong>: Add and manage donor information</li>
            <li><strong>Donation Records</strong>: Record various types of donated items</li>
            <li><strong>Distribution Records</strong>: Track the distribution of donated items</li>
            <li><strong>Reports & Statistics</strong>: View inventory and donor statistical reports</li>
          </ul>
          <p style={{ margin: '8px 0 0 0', color: '#666' }}>
            The system automatically calculates inventory balance to ensure distribution quantities do not exceed received quantities.
          </p>
        </div>
      </Card>

      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Donors"
              value={statistics?.totalDonors || 0}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Donations"
              value={statistics?.totalDonations || 0}
              prefix={<GiftOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Distributions"
              value={statistics?.totalDistributions || 0}
              prefix={<ShareAltOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Donation Value"
              value={statistics?.totalDonationValue || 0}
              prefix={<DollarOutlined />}
              suffix="dollars"
              precision={2}
              valueStyle={{ color: '#cf1322' }}
            />
          </Card>
        </Col>
      </Row>

      {/* Charts Section - Row 1: Allocation vs Donation Types */}
      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col xs={24} lg={12}>
          <Card title="Allocated Percentage by Type" size="small" bodyStyle={{ padding: 12 }} style={{ height: '100%' }}>
            {(() => {
              const rows = (statistics?.donationTypeStats || []) as any[];
              if (!rows.length) {
                return (
                  <div style={{ textAlign: 'center', padding: '50px', color: '#999' }}>
                    No data available
                  </div>
                );
              }
              // 直接把百分比转换为 0-100 的数值，避免回调取值差异导致显示 0
              const data = rows.map((r: any) => ({
                type: r.type,
                percentPct: Number(
                  r.percentPct !== undefined && r.percentPct !== null
                    ? r.percentPct
                    : (Number(r.percentAllocated || 0) * 100)
                ) || 0,
              }));
              return (
                <Column
                  data={data}
                  xField="type"
                  yField="percentPct"
                  colorField="type"
                  color={(datum: any) => donationTypeColors[datum.type] || '#69c0ff'}
                  height={320}
                  label={{
                    position: 'top',
                    fields: ['percentPct'],
                    formatter: (val: number) => `${Number(val).toFixed(2)}%`,
                  }}
                  meta={{ percentPct: { min: 0, max: 100 } }}
                  yAxis={{
                    min: 0,
                    max: 100,
                    label: {
                      formatter: (v: any) => `${Number(v).toFixed(2)}%`,
                    },
                  }}
                  tooltip={{
                    fields: ['type', 'percentPct'],
                    formatter: (type: string, val: number) => ({
                      name: type,
                      value: `${Number(val).toFixed(2)}%`,
                    }),
                  }}
                />
              );
            })()}
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="Donation Types Distribution" size="small" bodyStyle={{ padding: 12 }} style={{ height: '100%' }}>
            {getDonationTypeData().length > 0 ? (
              <Column
                data={getDonationTypeData()}
                xField="type"
                yField="value"
                colorField="type"
                color={(datum: any) => donationTypeColors[datum.type] || '#69c0ff'}
                height={320}
              />
            ) : (
              <div style={{ textAlign: 'center', padding: '50px', color: '#999' }}>
                No donation data available
              </div>
            )}
          </Card>
        </Col>
      </Row>

      {/* Removed Donor Distribution and Monthly Trends as requested */}

      {/* Reports Section: Inventory + Donor Reports */}
      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col xs={24} lg={12}>
          <Card title="Inventory Report (by type)" size="small" bodyStyle={{ padding: 12 }}>
            <Table
              size="middle"
              bordered
              rowKey={(r) => `${r.donationType}`}
              dataSource={inventoryReport}
              pagination={{ defaultPageSize: 5, showSizeChanger: true, position: ['bottomRight'] }}
              columns={[
                { title: 'Type', dataIndex: 'donationType', key: 'donationType', width: 140 },
                { title: 'Total', dataIndex: 'totalReceived', key: 'totalReceived', width: 120, align: 'right' as const },
                { title: 'Distributed', dataIndex: 'totalDistributed', key: 'totalDistributed', width: 140, align: 'right' as const },
                { title: 'Remaining', dataIndex: 'remainingQuantity', key: 'remainingQuantity', width: 140, align: 'right' as const },
                { title: 'Unit', dataIndex: 'unit', key: 'unit', width: 100 },
              ]}
            />
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="Donor Report (total contributions)" size="small" bodyStyle={{ padding: 12 }}>
            <Table
              size="middle"
              bordered
              rowKey={(r) => `${r.donorId}`}
              dataSource={donorReport}
              scroll={{ x: 'max-content' }}
              pagination={{ defaultPageSize: 5, showSizeChanger: true, position: ['bottomRight'] }}
              columns={[
                { title: 'Donor', dataIndex: 'donorName', key: 'donorName', width: 180, ellipsis: true },
                { title: 'Total Donations', dataIndex: 'totalDonations', key: 'totalDonations', width: 120, align: 'right' as const },
                { title: 'Total Value', dataIndex: 'totalValue', key: 'totalValue', width: 120, align: 'right' as const },
                { title: 'First Donation', dataIndex: 'firstDonationDate', key: 'firstDonationDate', width: 160, render: (v: string) => v ? v.replace('T', ' ').slice(0,16) : '-', align: 'center' as const },
                { title: 'Last Donation', dataIndex: 'lastDonationDate', key: 'lastDonationDate', width: 160, render: (v: string) => v ? v.replace('T', ' ').slice(0,16) : '-', align: 'center' as const },
              ]}
            />
          </Card>
        </Col>
      </Row>

      {getMonthlyTrendData().length > 0 && (
        <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
          <Col xs={24}>
            <Card title="Monthly Trends" size="small" bodyStyle={{ padding: 12 }}>
              <Line
                data={getMonthlyTrendData()}
                xField="month"
                yField="value"
                seriesField="type"
                color={['#5B8FF9','#5AD8A6','#9270CA','#F6BD16','#E8684A','#6DC8EC','#FF9D4D']}
                height={320}
              />
            </Card>
          </Col>
        </Row>
      )}

      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col xs={24} lg={12}>
          <Card title="System Status" size="small" bodyStyle={{ padding: 12 }}>
            <div style={{ padding: '16px 0' }}>
              <Space direction="vertical" size="small">
                <div>
                  <Tag color={backendOk ? 'green' : 'red'} style={{ marginRight: 8 }}>
                    {backendOk ? 'OK' : 'DOWN'}
                  </Tag>
                  Backend service
                </div>
                <div>
                  <Tag color={dbOk ? 'green' : 'red'} style={{ marginRight: 8 }}>
                    {dbOk ? 'OK' : 'DOWN'}
                  </Tag>
                  Database connectivity
                </div>
                <div>
                  <Tag color={'green'} style={{ marginRight: 8 }}>OK</Tag>
                  Frontend UI
                </div>
                <div>
                  <Tag color={(statistics && !loading) ? 'blue' : 'default'} style={{ marginRight: 8 }}>INFO</Tag>
                  Statistics {statistics ? 'loaded' : 'not loaded'}
                </div>
              </Space>
            </div>
          </Card>
        </Col>
        
        <Col xs={24} lg={12}>
          <Card title="Quick Actions" size="small" bodyStyle={{ padding: 12 }}>
            <div style={{ padding: '16px 0' }}>
              <Space wrap>
                <Button type="primary" onClick={() => navigate('/donors')}>
                  Add new donor
                </Button>
                <Button onClick={() => navigate('/donations')}>
                  Register donation
                </Button>
                <Button onClick={() => navigate('/distributions')}>
                  Record distribution
                </Button>
                <Button onClick={() => navigate('/reports')}>
                  View reports
                </Button>
              </Space>
            </div>
          </Card>
        </Col>
      </Row>

      
    </div>
  );
};

export default Dashboard;
