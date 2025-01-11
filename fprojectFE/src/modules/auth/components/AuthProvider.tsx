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