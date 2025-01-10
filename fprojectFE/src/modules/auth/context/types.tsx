export interface User{
    email:string;
    roles:string[];
    permissions:string[];
    name:string;
    avt?:string;
}
export interface AuthState{
    isAuthenticated?:boolean;
    isInitialized?:boolean;
    user:User|null;
}