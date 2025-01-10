import { FC, lazy } from 'react';
import { Navigate, useRoutes } from 'react-router-dom';
import { CreateRoom, DashBoard, ListRoom } from '../modules/account/pages';
import LayoutDefault from '../Layout/LayoutDefault';
import GuestGuard from '../modules/auth/components/GuestGuard';
import AuthGuard from '../modules/auth/components/AuthGuard';
import RoleBasedGuard from '../modules/auth/components/RoleBasedGuard';
import { ROLE } from '../config';
import { Signup } from '../modules/auth/pages';
const Signin = lazy(() => import('../modules/auth/pages/SigninPage'));


const Router: FC = () => {
  return useRoutes([
    {
      path: 'auth',
      children: [
        {
          path: 'sign-in',
          element: <GuestGuard><Signin /></GuestGuard>,
        },
      ],
    },
    {
      path: 'auth',
      children: [
        {
          path: 'sign-up',
          element: <GuestGuard><Signup /></GuestGuard>,
        },
      ],
    },
    {
      element:<AuthGuard>
                <LayoutDefault></LayoutDefault>
              </AuthGuard>,
      children:[
        {
            path: 'account',
            children: [
              {
                index: true,
                path:'/account',
                element: <DashBoard/>,
              },
              {
                path: 'my-store',
                children: [
                  {
                    path:"list-product",
                    element: <RoleBasedGuard accessibleRoles={[ROLE.ADMIN]}><ListRoom/></RoleBasedGuard>,
                    children:[
                      {
                        path:"create",
                        element:<CreateRoom/>,
                      }
                    ] 
                  },
                  {
                    path: 'list',
                    element: <Navigate to="/account/my-store/category" replace />,
                  },
                  {
                    path: ':id/edit',
                    element: <Navigate to="/account/my-store/:id/edit" replace />,
                  },
                ],
              },
            ],
          
        }
      ]

    },
  ]);
};

export default Router;
