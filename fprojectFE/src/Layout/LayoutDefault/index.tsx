import { Layout } from "antd";
// import LearnGrid from "../../Components/LearnGrid";
import "./layoutdefault.css";
import logo_short from "../../images/logo_short.png";
import logo_long from "../../images/logo_long.png";
import {SearchOutlined, MenuUnfoldOutlined} from "@ant-design/icons"
import { useState } from "react";
import Notify from "../../Components/Notify";
import MenuSider from "../../Components/MenuSider";
import { Outlet } from "react-router-dom";

const {Sider,Content}=Layout;

function LayoutDefault(){
    const [collapsed,setCollapsed]=useState(false);

    
    return(
        <>
            <Layout className="layout-default">
                <header className="header">
                    <div className={"header__logo "+(collapsed && "header__logo--collapsed")}>
                        <img src={collapsed ? logo_short : logo_long} alt="Logo"/>
                    </div>

                    <div className="header__nav">
                        <div className="header__nav-left">

                            <div className="header__collapse" onClick={()=>setCollapsed(!collapsed)}>
                                <MenuUnfoldOutlined></MenuUnfoldOutlined>
                            </div>
                            <div className="header__search">
                                <SearchOutlined></SearchOutlined>
                            </div>

                        </div>

                        <div className="header__nav-right">
                            <Notify/>
                        </div>

                    </div>


                </header>
                <Layout>
                    <Sider width={140} className="sider" 
                    theme="light"
                            collapsedWidth={50} collapsed={collapsed}
                            style={{ 
                                transition: 'all 0.5s' // Thêm transition
                              }}
                     >
                        <MenuSider></MenuSider>
                    </Sider>

                    <Content className="content">
                        <Outlet></Outlet>
                    </Content>

                </Layout>

            </Layout>

        </>
    )
}

export default LayoutDefault