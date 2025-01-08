import { Button } from 'antd';
import React, { useEffect, useState } from 'react';
import { GoogleOutlined } from "@ant-design/icons";
import { FE_URL } from '../../config/api';



const GoogleLogin:React.FC=() => {
  const [accessToken, setAccessToken] = useState(null);
  useEffect(() => {
    // Sau khi Google redirect, đọc token từ URL
    const hash = window.location.hash;
    console.log("11111")
    if (hash) {
      const params = new URLSearchParams(hash.substring(1));
      const act = params.get("access_token");
      if (act) {
        console.log("Access Token:", act);
        setAccessToken(act)
        // Gửi Access Token đến Backend
        fetch("http://localhost:8080/google/login/user", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ "accessToken":act }),
        })
          .then((response) => response.json())
          .then((data) => {
            console.log("Server Response:", data);
            if (data.status === "success") {
              alert(`Login Successful! Welcome, ${data.user.name}`);
            } else {
              alert("Login Failed!");
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
      }
    }
  }, []);

  const handleLogin = () => {
    const clientId = '82696225190-hdskhqludierkcsj1r4h9cif8kb6lq2t.apps.googleusercontent.com'; // Thay bằng client ID của bạn
    const redirectUri = `${FE_URL}/signin`; // Thay bằng redirect URI của bạn
    const scope = 'openid profile email'; // Các quyền yêu cầu từ Google

    // Tạo URL yêu cầu đăng nhập Google
    const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=token&scope=${scope}&prompt=login`;

    // Chuyển hướng đến Google OAuth2
    window.location.href = authUrl;
  };

  return (
    <div>
      {accessToken ? (
        <div>
          <h3>Logged in!</h3>
        </div>
      ) : (
        <div>          
          <Button
              type="primary"
              size="small"
              icon={<GoogleOutlined/>}
              onClick={handleLogin}
              style={{ width: '300px', height: '40px' }}

              variant='filled' 
              shape='round' 
              
          >
            Login with your Google Account 
          </Button>
        </div>
      )}
    </div>
  );
}

export default GoogleLogin;
