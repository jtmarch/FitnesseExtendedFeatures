<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.nl.ilionx.com/">
    <soapenv:Header/>
    <soapenv:Body>
        <soap:doSomething>
            <value>${valueForTemplate}</value>
            <secondValue>&lt;Symbol&gt;helloworld&lt;/Symbol&gt;</secondValue>
        </soap:doSomething>
    </soapenv:Body>
</soapenv:Envelope>
