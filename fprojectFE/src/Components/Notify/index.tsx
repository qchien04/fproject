import { BellOutlined } from '@ant-design/icons';
// import { SettingOutlined } from '@ant-design/icons';

import { Badge, Button, Dropdown } from 'antd';
import "./Notify.css";

function Notify(){
    // const items2 = [
    //     {
    //       key: '1',
    //       label: 'My Account',
    //       disabled: true,
    //     },
    //     {
    //       type: 'divider',
    //     },
    //     {
    //       key: '2',
    //       label: <a href='https://www.facebook.com/' target="_blank" rel="noopener noreferrer">Facebook</a>,
    //       extra: '⌘F',
    //     },
    //     {
    //       key: '3',
    //       label: <a href='https://www.youtube.com/' target="_blank" rel="noopener noreferrer">Youtube</a>,
    //       extra: '⌘Y',
    //     },
    //     {
    //       key: '4',
    //       label: <a href='https://www.chatgpt.com/'target="_blank" rel="noopener noreferrer">ChatGPT</a>,
    //       icon: <SettingOutlined />,
    //       extra: '⌘C',
    //     },
    //   ];


    const items = [
    {
        key: '1',
        label: (<div className='notify__item'>
        <div className='notify__item-icon'>
            <BellOutlined></BellOutlined>
        </div>
        <div className='notify__item--content'>
            <div className='notify__item-title'>
                Item1
            </div>
            <div className='notify__item-time'>
                1s truoc
            </div>
        </div>
        </div>),
        extra: '⌘F',
    },
    {
        key: '2',
        label: (<div className='notify__item'>
            <div className='notify__item-icon'>
                <BellOutlined></BellOutlined>
            </div>
            <div className='notify__item--content'>
                <div className='notify__item-title'>
                    Item1
                </div>
                <div className='notify__item-time'>
                    1s truoc
                </div>
            </div>
            </div>),
        extra: '⌘Y',
    },
    {
        key: '3',
        label: (<div className='notify__item'>
            <div className='notify__item-icon'>
                <BellOutlined></BellOutlined>
            </div>
            <div className='notify__item--content'>
                <div className='notify__item-title'>
                    Item1
                </div>
                <div className='notify__item-time'>
                    1s truoc
                </div>
            </div>
            </div>),
        extra: '⌘C',
    },
    ];


    return(
        <>
            <Dropdown menu={{items:items}} 
                trigger={["click"]}
                dropdownRender={(menu)=>(
                    <>
                        <div className='notify__dropdown'>
                            <div className='notify__header'>
                                <div className='notify__header-title'>
                                    <BellOutlined></BellOutlined>
                                    Notifition
                                </div>
                                <Button type='link'>View All</Button>
                            </div>
                            <div className='notify__body'>
                                {menu}
                            </div>
                        </div>
                    </>
                )}
                >
                <Badge dot={true}>
                    <Button type='text' icon={<BellOutlined/>}></Button>
                </Badge>
            </Dropdown>
        </>
    );
}

export default Notify