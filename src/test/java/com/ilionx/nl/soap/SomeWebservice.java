package com.ilionx.nl.soap;

import javax.jws.WebService;

/**
 * Created by jdebruin on 9/18/15.
 */
@WebService(endpointInterface = "com.ilionx.nl.soap.SomeWebserviceInterface")
public class SomeWebservice implements SomeWebserviceInterface {

    public String doSomething(String value){
        return value;
    }

}
