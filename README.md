:# Plugin SDK for Java

If you are developing your own plugin for your Staffbase app we describe the authentication flow of a plugin at https://developers.staffbase.com/api/plugin-sso/. While this documentation just covers the conceptual ideas of the interface of plugins though – the so called Plugin SSO – we want to provide a library to help you develop your first plugin for Staffbase even faster. This SDK provides the basic functionality to parse and verify a provided token for Java.

## Installation

We provide our Plugin SDK via Maven Central Repository (https://repo1.maven.org/maven2/com/staffbase/plugins-sdk-java/). Thus, you can just use maven for installation:

### Maven

```xml
<dependency>
    <groupId>com.staffbase</groupId>
    <artifactId>plugins-sdk-java</artifactId>
    <version>1.1.1</version>
</dependency>
```

### Gradle

```
dependencies {
    compile 'com.staffbase:plugins-sdk-java:1.1.1'
}
```

## API Reference

For the API reference of this SDK please consult the [docs](https://staffbase.github.io/plugins-sdk-java).

## Code Example

You can try to create a token from the received jwt.

```java
	import com.staffbase.plugins.sdk.sso.SSOData;
	import com.staffbase.plugins.sdk.sso.SSOFacade;

	...

	String jwToken = ...
	RSAPublicKey rsaPublicKey = ...

 	try {
		final SSOFacade ssoFac = SSOFacade.create(rsaPublicKey);
		final SSOData ssoData = ssoFac.verify(jwToken);
		
		// If the plugin instance was deleted in Staffbase
		if(ssoData.isDeleteInstanceCall()){
		    this.handleSsoDeletionCall(ssoData.getInstanceID());
		    return;
		}
		
		request.setAttribute("instanceID", ssoData.getInstanceID());

		return this.forward("/index.jsp");

	} catch (final SSOException ssoException) {
		
		if (logger.isErrorEnabled()) {
			logger.error("Verification of SSO attempt failed. Aborting.", ssoException);
		}

		return this.forward("/errors/403-Forbidden.jsp");
	}
```
## Contribution

- Fork it
- Create a branch `git checkout -b feature-description`
- Put your name into authors.txt
- Commit your changes `git commit -am "Added ...."`
- Push to the branch `git push origin feature-description`
- Open a Pull Request

## Running Tests

To run the tests a simple `# mvn test` command in the root directory will suffice.

## License

Copyright 2017 Staffbase GmbH.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
