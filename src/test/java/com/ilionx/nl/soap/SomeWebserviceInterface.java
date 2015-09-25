package com.ilionx.nl.soap;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface SomeWebserviceInterface {

    String doSomething(@WebParam(name = "value") String value);

}
