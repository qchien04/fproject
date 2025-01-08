import { Button, notification, Spin } from "antd";
import { useState } from "react";
import { Form, Input } from "antd";
import GoogleLogin from "../GoogleLogin";
import { BASE_API_URL } from "../../../config/api";
import "./Signin.css"; // Import file CSS

interface Logindata {
  username: string;
  password: string;
}

const Signin: React.FC = () => {
  const [notifyApi, contextHolder] = notification.useNotification();
  const [spinning, setSpinning] = useState<boolean>(false);

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
    const response = await fetch(`${BASE_API_URL}/auth/signin`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });
    setSpinning(true);
    const data=await response.json();
    setTimeout(() => {
      if (response) {
        notifyApi.success({
          message: `Success`,
          description: `Login successfully`,
          duration: 5,
          placement: "bottomRight",
        });
        console.log(data);
      } else {
        notifyApi.error({
          message: "Failed",
          description: `Login failed`,
          duration: 5,
        });
      }
      setSpinning(false);
    }, 1000);
  };

  return (
    <div className="center-container">
      <Spin spinning={spinning} tip="Getting login information">
        <h2>Login</h2>
        {contextHolder}
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
        </Form>
      </Spin>
      <div className="google-login">
        <GoogleLogin/>
      </div>
    </div>
  );
};

export default Signin;
