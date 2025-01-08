
import LayoutDefault from "../Layout/LayoutDefault";
import BookRoom from "../pages/BookRoom";
import CreateRoom from "../pages/CreateRoom";
import DashBoard from "../pages/Dashboard";
import ListRoom from "../pages/ListRoom";
import Authmail from "../pages/LoginPage/Authmail";
import Signin from "../pages/LoginPage/SigninPage/Signin";
import Signup from "../pages/LoginPage/Signup";

export const routes=[
    {
        path:"signin",
        element:<Signin></Signin>
    },
    {
        path:"signup",
        element:<Signup></Signup>,
        children:[
            {
                path:"authmail",
                element:<Authmail></Authmail>
            }
        ]
    },
    {
        path:"/",
        element:<LayoutDefault></LayoutDefault>,
        children:[
            {
                path:"/",
                element:<DashBoard></DashBoard>
            },
            {
                path:"book-room",
                element:<BookRoom></BookRoom>,
            },
            {
                path:"create-room",
                element:<CreateRoom></CreateRoom>,
            },
            {
                path:"rooms",
                element:<ListRoom></ListRoom>,
            },
        ]
    }
]