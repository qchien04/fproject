import { FC, ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { Spin } from "antd";

const GuestGuard: FC<{ children: ReactNode }> = ({ children }) => {
  const { isInitialized, isAuthenticated } = useAuth();

  if (!isInitialized) return (
    <Spin spinning={true} tip="Loading...">
      <div></div>
    </Spin>
  );
  if (isAuthenticated) return <Navigate to="/account" />;

  return <>{children}</>;
};

export default GuestGuard;
