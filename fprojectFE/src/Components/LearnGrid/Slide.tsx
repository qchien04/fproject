import { Carousel, QRCode } from "antd";
import logo from "../../assets/images/logo_short.png";

function Slide(){
    return(
        <>
        <Carousel autoplay dotPosition="top" dots={true} effect="fade" style={{width:"200px",height:"400px"}}>
            <div className="slider-item">1</div>
            <div className="slider-item">2</div>
            <div className="slider-item">3</div>
            <div className="slider-item">4</div>
            <QRCode className="slider-item"
            errorLevel="H"
            value="https://www.facebook.com/?locale=vi_VN"
            icon={logo}
            // style={{width:"100px", height:"100px"}}
            >
                
            </QRCode>
        </Carousel>
        </>
    );
}

export default Slide