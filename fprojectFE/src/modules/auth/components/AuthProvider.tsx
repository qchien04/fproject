import { FC, ReactNode, useEffect, useReducer } from "react";
import { User } from "../context/types";
import { AuthContext, initialState } from "../context/AuthContext";
import { initialize, reducer } from "../context/AuthReducer";
import authService from "../services/authService";
//import userService from "../../account/services/userServicer";
interface ChildrenProps{
    children:ReactNode;
};
const AuthProvider: FC<ChildrenProps> = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState);
  
    useEffect(() => {
      (async () => {
        const accessToken = localStorage.getItem('jwtToken');
        if (!accessToken) {
          return dispatch(initialize({ isAuthenticated: false, user: null }));
        }
  
        try {
          const user:User=await authService.getbasicInfo()
        //   ob={
        //     email:"tqchien04@gmail.com",
        //     roles:["user"],
        //     permissions:["all"],
        //     name:"chien quang",
        //     avt:"https://scontent.fhph2-1.fna.fbcdn.net/v/t39.30808-1/318092606_822166405526578_8470641239927952714_n.jpg?stp=dst-jpg_s160x160_tt6&_nc_cat=109&ccb=1-7&_nc_sid=e99d92&_nc_ohc=m2JN1iW7BToQ7kNvgFHRuf7&_nc_oc=AdjhXtyyAyJPKI9_pPxx8_33k9pOhco4DA8Osd_U1xNKiKwzFv2YYA0qrW_r7EOtaq694OxsUjGqjeMDkbS-RmC_&_nc_zt=24&_nc_ht=scontent.fhph2-1.fna&_nc_gid=AKvmeh0sukOkFJbPhBYjxKu&oh=00_AYDPIbdgfxKf2BbeJblWANPrRIQF2MNvbJnKTQj4UpD6QA&oe=6785E3CA",
        // }
          dispatch(initialize({ isAuthenticated: true, user }));
        } catch {
          dispatch(initialize({ isAuthenticated: false, user: null }));
        }
      })();
    }, []);
  
    return (
      <AuthContext.Provider value={{ ...state, dispatch }}>
        {children}
      </AuthContext.Provider>
    );
};
export default AuthProvider;