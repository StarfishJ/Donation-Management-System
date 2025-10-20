/**
 * Main Application Component
 * Root component of the Donation Management System
 */

import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate, useLocation } from 'react-router-dom';
import { ConfigProvider, Layout, Menu, theme } from 'antd';
import { 
  UserOutlined, 
  GiftOutlined, 
  ShareAltOutlined, 
  BarChartOutlined,
  HomeOutlined 
} from '@ant-design/icons';
import zhCN from 'antd/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';

// Import page components
import Dashboard from './pages/Dashboard';
import DonorManagement from './pages/DonorManagement';
import DonationManagement from './pages/DonationManagement';
import DistributionManagement from './pages/DistributionManagement';
import Reports from './pages/Reports';

// Set dayjs to Chinese
dayjs.locale('zh-cn');

const { Header, Content, Sider } = Layout;

// Internal component that uses hooks
const AppContent: React.FC = () => {
  const [collapsed, setCollapsed] = React.useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  // Menu items configuration
  const menuItems = [
    {
      key: '/',
      icon: <HomeOutlined />,
      label: 'Dashboard',
    },
    {
      key: '/donors',
      icon: <UserOutlined />,
      label: 'Donor Management',
    },
    {
      key: '/donations',
      icon: <GiftOutlined />,
      label: 'Donation Records',
    },
    {
      key: '/distributions',
      icon: <ShareAltOutlined />,
      label: 'Distribution Records',
    },
    {
      key: '/reports',
      icon: <BarChartOutlined />,
      label: 'Reports & Statistics',
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      {/* Sidebar */}
      <Sider 
        collapsible 
        collapsed={collapsed} 
        onCollapse={setCollapsed}
        theme="dark"
        width={240}
        collapsedWidth={72}
      >
        <div style={{ 
          height: 32, 
          margin: 16, 
          background: 'rgba(255, 255, 255, 0.2)',
          borderRadius: 6,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'white',
          fontWeight: 'bold'
        }}>
          {collapsed ? 'DMS' : 'Donation Management System'}
        </div>
        <Menu
          theme="dark"
          selectedKeys={[location.pathname]}
          mode="inline"
          items={menuItems}
          style={{ fontSize: 16 }}
          onClick={({ key }) => {
            navigate(key);
          }}
        />
      </Sider>

      <Layout>
        {/* Top Navigation */}
        <Header style={{ 
          padding: 0, 
          background: colorBgContainer,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          paddingLeft: 16,
          paddingRight: 16
        }}>
          <h2 style={{ margin: 0, color: '#1890ff' }}>
            Donation Management System
          </h2>
        </Header>

        {/* Main Content */}
        <Content style={{ margin: '16px' }}>
          <div style={{
            padding: 20,
            minHeight: 360,
            background: colorBgContainer,
            borderRadius: 8,
          }}>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/donors" element={<DonorManagement />} />
              <Route path="/donations" element={<DonationManagement />} />
              <Route path="/distributions" element={<DistributionManagement />} />
              <Route path="/reports" element={<Reports />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

const App: React.FC = () => {
  return (
    <ConfigProvider locale={zhCN}>
      <Router>
        <AppContent />
      </Router>
    </ConfigProvider>
  );
};

export default App;