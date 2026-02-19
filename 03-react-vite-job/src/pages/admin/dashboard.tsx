import { useEffect, useMemo, useState } from "react";
import { Card, Col, List, Row, Statistic, Tag, Typography, notification } from "antd";
import { ApartmentOutlined, FileTextOutlined, TeamOutlined } from "@ant-design/icons";
import CountUp from 'react-countup';
import { callFetchCompany, callFetchJob, callFetchResume } from "@/config/api";
import { IJob } from "@/types/backend";

const DashboardPage = () => {
    const [stats, setStats] = useState({
        companies: 0,
        jobs: 0,
        resumes: 0,
    });
    const [recentJobs, setRecentJobs] = useState<IJob[]>([]);
    const [recentResumes, setRecentResumes] = useState<any[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const formatter = (value: number | string) => (
        <CountUp end={Number(value)} separator="," />
    );

    useEffect(() => {
        const init = async () => {
            try {
                setLoading(true);
                const [companyRes, jobRes, resumeRes] = await Promise.all([
                    callFetchCompany("page=1&size=1"),
                    callFetchJob("page=1&size=5&sort=createdAt,desc"),
                    callFetchResume("page=1&size=5&sort=createdAt,desc"),
                ]);

                setStats({
                    companies: companyRes?.data?.meta?.total ?? 0,
                    jobs: jobRes?.data?.meta?.total ?? 0,
                    resumes: resumeRes?.data?.meta?.total ?? 0,
                });

                setRecentJobs(jobRes?.data?.result ?? []);
                setRecentResumes(resumeRes?.data?.result ?? []);
            } catch (error) {
                notification.error({
                    message: "Không thể tải dữ liệu dashboard",
                    description: "Vui lòng thử lại hoặc kiểm tra kết nối."
                });
            } finally {
                setLoading(false);
            }
        };

        init();
    }, []);

    const activeJobRate = useMemo(() => {
        if (!recentJobs.length) return 0;
        const activeCount = recentJobs.filter(job => job.active).length;
        return Math.round((activeCount / recentJobs.length) * 100);
    }, [recentJobs]);

    return (
        <>
            <Row gutter={[20, 20]}>
                <Col span={24} md={6}>
                    <Card title="Doanh nghiệp" bordered={false} loading={loading}>
                        <Statistic
                            prefix={<ApartmentOutlined />}
                            title="Tổng số công ty"
                            value={stats.companies}
                            formatter={formatter}
                        />
                    </Card>
                </Col>
                <Col span={24} md={6}>
                    <Card title="Công việc" bordered={false} loading={loading}>
                        <Statistic
                            prefix={<TeamOutlined />}
                            title="Tổng số job"
                            value={stats.jobs}
                            formatter={formatter}
                        />
                        <div style={{ marginTop: 12, color: '#8c8c8c' }}>
                            Tỷ lệ đang mở (5 job mới): {activeJobRate}%
                        </div>
                    </Card>
                </Col>
                <Col span={24} md={6}>
                    <Card title="Hồ sơ ứng tuyển" bordered={false} loading={loading}>
                        <Statistic
                            prefix={<FileTextOutlined />}
                            title="Tổng số hồ sơ"
                            value={stats.resumes}
                            formatter={formatter}
                        />
                    </Card>
                </Col>
                <Col span={24} md={6}>
                    <Card title="Job" bordered={false} loading={loading}>
                        <Statistic
                            title="Job mới nhất"
                            value={recentJobs[0]?.name || "--"}
                        />

                    </Card>
                </Col>
            </Row>

            <Row gutter={[20, 20]} style={{ marginTop: 24 }}>
                <Col span={24} md={12}>
                    <Card title="Job mới nhất" bordered={false} loading={loading}>
                        <List
                            dataSource={recentJobs}
                            renderItem={(item) => (
                                <List.Item>
                                    <List.Item.Meta
                                        title={<Typography.Text strong>{item.name}</Typography.Text>}
                                        description={`${item.company?.name ?? 'N/A'} • ${item.location}`}
                                    />
                                    <Tag color={item.active ? 'green' : 'red'}>
                                        {item.active ? 'Đang mở' : 'Đã đóng'}
                                    </Tag>
                                </List.Item>
                            )}
                        />
                    </Card>
                </Col>
                <Col span={24} md={12}>
                    <Card title="CV gửi gần đây" bordered={false} loading={loading}>
                        <List
                            dataSource={recentResumes}
                            renderItem={(item: any) => (
                                <List.Item>
                                    <List.Item.Meta
                                        title={<Typography.Text strong>{item?.job?.name ?? '---'}</Typography.Text>}
                                        description={item?.companyName ?? '---'}
                                    />
                                    <Tag color={item.status === 'PENDING' ? 'blue' : item.status === 'REVIEWED' ? 'gold' : 'green'}>
                                        {item.status}
                                    </Tag>
                                </List.Item>
                            )}
                        />
                    </Card>
                </Col>
            </Row>
        </>
    )
}

export default DashboardPage;