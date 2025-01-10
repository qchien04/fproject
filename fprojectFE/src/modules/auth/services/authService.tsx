import axiosClient from '../../../config/axiosconfig';

interface User{
    email:string;
    roles:string[];
    permissions:string[];
    name:string;
    avt?:string;
}

export interface AuthDTO{
    jwtToken:string,
    user:User
}

export interface LoginForm{
    username:string,
    password:string
}

export interface SigninResponse{
    jwt:string,
    isAuth:boolean
}

interface AuthoritiesResponse{
    email:string;
    avt?:string;
    name:string;
    roles:string[];
    permissions:string[];
  }
const authService = {
  getbasicInfo: async (): Promise<AuthoritiesResponse> => {
    try {
      const response:AuthoritiesResponse = await axiosClient.get('/api/users/basicInfo');
      return response; 
    } catch (error) {
      console.error('Error fetching Authorities:', error);
      throw error; 
    }
  },

  signin: async (data: LoginForm): Promise<SigninResponse> => {
    try {
      const response: SigninResponse = await axiosClient.post('/auth/signin', data);
      return response;
    } catch (error) {
      console.error('Error login: ', error);
      throw error;
    }
  },

  signingoogle: async (accessToken:string): Promise<SigninResponse> => {
    try {
      const response: SigninResponse = await axiosClient.post('/google/login/user', {accessToken});
      return response;
    } catch (error) {
      console.error('Error login: ', error);
      throw error;
    }
  },

};
export default authService;