package ingokuba.wolpertinger.boundary;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import ingokuba.wolpertinger.boundary.WolpertingerApplication.Roles;

@ApplicationScoped
@DeclareRoles(Roles.ADMIN)
@ApplicationPath("rest")
@BasicAuthenticationMechanismDefinition(realmName = "wolpertinger")
public class WolpertingerApplication extends Application
{

    public class Roles
    {

        private Roles()
        {
            throw new UnsupportedOperationException("static class");
        }

        public static final String ADMIN = "admin";
    }
}