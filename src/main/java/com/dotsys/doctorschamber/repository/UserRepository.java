package com.dotsys.doctorschamber.repository;

import com.dotsys.doctorschamber.Factory.UserFactory;
import com.dotsys.doctorschamber.Models.LoginInfo;
import com.dotsys.doctorschamber.Models.UserInfo;
import com.dotsys.doctorschamber.util.APIClientService;
import com.dotsys.doctorschamber.util.Global;

/**
 * Created by Azad on 17-Jul-17.
 */

public class UserRepository {

    APIClientService service;
    UserFactory factory;

    public UserRepository(){
        service = new APIClientService();
        factory = new UserFactory();
    }


    public boolean Register(UserInfo userInfo){
        return service.Register(userInfo);
    }

    public boolean Login(String email, String password, String user_type){
        try {
            LoginInfo loginInfo = new LoginInfo(email, password, user_type);
            if(service.Login(loginInfo)){
                return factory.Save(Global.loggedInUser);
            }
            return false;
        }catch (Exception e){
            throw e;
        }

    }

    public boolean Logout(){
        try{
            LoginInfo loginInfo = new LoginInfo(Global.loggedInUser.GET_userEmail()
                    , Global.loggedInUser.GET_userPass()
                    , Global.loggedInUser.GET_userType());
            if(service.Logout(loginInfo)){
                return factory.deleteAll();
            }
            return false;
        }catch (Exception e){
            throw e;
        }

    }
}
