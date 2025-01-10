import { Button } from "antd";
// import { notification,Spin } from "antd";
// import { useState } from "react";
import { Form, Input } from "antd";
import GoogleLogin from "./GoogleLogin";

import "./Signin.css";
import authService from "../../services/authService";
import { useAuth } from "../../hooks/useAuth";
import { signIn } from "../../context/AuthReducer";
import { useNavigate } from "react-router-dom";

interface Logindata {
  username: string;
  password: string;
}
interface User{
  email:string;
  roles:string[];
  permissions:string[];
  name:string;
  avt?:string;
}
const Signin: React.FC = () => {
 // const [notifyApi, contextHolder] = notification.useNotification();
  // const [spinning, setSpinning] = useState<boolean>(false);
  const navigate=useNavigate();
  const { dispatch }= useAuth();

  console.log("compo signin");


  const rulesPassword = [
    {
      required: true,
      message: "Not accept !",
    },
  ];
  const rulesUsername = [
    {
      required: true,
      message: "Not accept !",
    },
  ];
  const handleSubmit = async (values: Logindata) => {
    try{
      const response = await authService.signin(values);
      console.log(response);
      localStorage.setItem("jwtToken",response.jwt);

      const user:User=await authService.getbasicInfo();
      console.log(user);

      dispatch(signIn({user}));
      
      // setTimeout(() => {
      //   if (response) {
      //     notifyApi.success({
      //       message: `Success`,
      //       description: `Login successfully`,
      //       duration: 5,
      //       placement: "bottomRight",
      //     });
      //   } 
      //   else {
      //     notifyApi.error({
      //       message: "Failed",
      //       description: `Login failed`,
      //       duration: 5,
      //     });
      //   }
      //   //setSpinning(false);
      // }, 3000);
    }
    catch (error){
      console.error(error);
    }

  };

  return (
    <div className="center-container">
      {/* <Spin spinning={spinning} tip="Getting login information"> */}
        <h2>Login</h2>
        {/* {contextHolder} */}
        <Form
          className="formlogin"
          name="login-form"
          layout="vertical"
          onFinish={handleSubmit}
          style={{ width: '400px', margin: '0 auto' }}
        >
          <Form.Item label="Username" name="username" rules={rulesUsername}>
            <Input />
          </Form.Item>

          <Form.Item label="Password" name="password" rules={rulesPassword}>
            <Input.Password />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit">
              Login
            </Button>
          </Form.Item>
            <div>Don't have account ?</div>
            <Button type="primary" onClick={() => navigate("/auth/sign-up")}>
              Sign up
            </Button>
        </Form>
      {/* </Spin> */}
      <div className="google-login">
        <GoogleLogin/>
      </div>
    </div>
  );
};

export default Signin;
