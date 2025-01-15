import { Carousel, Col, Collapse, Image, Rate, Row, Tabs, QRCode } from "antd";
import "./learngrid.css";
import Slide from "./Slide";
import logo from "../../assets/images/logo_short.png";

/* Ghi chú cho Row và Col:
   - gutter: khoảng cách giữa các cột
   - align: căn dọc
   - justify: căn ngang
   - wrap: tự động xuống dòng
   - Col:
     + offset: khoảng cách trống bên trái
     + order: thứ tự cột
     + span: số lượng cột mà box chiếm
     + responsive: xs, sm, md, lg, xl, xxl
*/

interface Item {
  key: string;
  label: string;
  children: React.ReactNode;
}

const items: Item[] = [
  { key: '1', label: 'Header 1', children: 'Nội dung của Panel 1' },
  { key: '2', label: 'Header 2', children: 'Nội dung của Panel 2' },
];

const items2: Item[] = [
  { key: '1', label: 'Header 1', children: 'Nội dung của Tab 1' },
  { key: '2', label: 'Header 2', children: 'Nội dung của Tab 2' },
  { key: '3', label: 'Header 3', children: <Slide /> },
];

function LearnGrid(): JSX.Element {
  return (
    <>
      <Row gutter={[5, 100]} style={{ marginBottom: '10px' }}>
        {Array(3).fill(0).map((_, index) => (
          <Col key={index} span={8}>
            <div className="box" style={{ border: '1px solid #ccc' }}>cột {index + 1}</div>
          </Col>
        ))}
      </Row>

      <Row gutter={[5, 50]} style={{ marginBottom: '10px' }}>
        {Array(3).fill(0).map((_, index) => (
          <Col key={index} span={8}>
            <div className="box" style={{ border: '1px solid #ccc' }}>cột {index + 1}</div>
          </Col>
        ))}
        <Col span={12}>
          <div className="box" style={{ border: '1px solid #ccc' }}>cột 4</div>
        </Col>
        <Col span={12}>
          <div className="box" style={{ border: '1px solid #ccc' }}>cột 5</div>
        </Col>
        <Col span={24}>
          <div className="box" style={{ border: '1px solid #ccc' }}>cột 6</div>
        </Col>
      </Row>

      <Carousel autoplay dotPosition="top" dots effect="fade">
        {Array(4).fill(0).map((_, index) => (
          <div className="slider-item" key={index}>{index + 1}</div>
        ))}
      </Carousel>

      <Collapse items={items} defaultActiveKey="1" />

      <Image 
        width={200} 
        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrTiecQuLhs_w5OQTOFmPxTqaxRua2SY9stg&s" 
        alt="Placeholder Image" 
      />

      <Tabs items={items2} />

      <QRCode
        errorLevel="H"
        value="https://www.facebook.com/?locale=vi_VN"
        icon={logo}
      />

      <Rate 
        defaultValue={2.5} 
        allowHalf 
        character="A"
        tooltips={["ngu", "ốc chó", "tạm", "hay", "chuẩn"]} 
        onChange={(value: number) => console.log(value)} 
      />
    </>
  );
}

export default LearnGrid;
