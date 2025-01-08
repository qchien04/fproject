import { useState } from 'react';
import { Button } from "antd";
import {ExpandAltOutlined} from "@ant-design/icons";


function LearnButton(){
    const  [loading,setLoading]=useState(false);

  const handleclick=()=>{
    setLoading(true);
    setTimeout(()=>{
      const result={
        code:200,
        data:[]
      };
      if(result&&result.code===200){
        setLoading(false);
      }
    },3000)
  }
  return (
    <>
      <Button variant='filled' icon={<ExpandAltOutlined rotate={45} spin={true}/>}  type='primary' shape='circle' size='large' loading={loading} onClick={handleclick} >ok</Button>
    </>
  );
}
export default LearnButton