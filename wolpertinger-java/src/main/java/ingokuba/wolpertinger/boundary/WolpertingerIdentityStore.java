package ingokuba.wolpertinger.boundary;

import static java.util.Arrays.asList;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

import java.util.HashSet;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import ingokuba.wolpertinger.boundary.WolpertingerApplication.Roles;

@ApplicationScoped
public class WolpertingerIdentityStore
    implements IdentityStore
{

    private static final Logger LOGGER = Logger.getLogger(WolpertingerIdentityStore.class.getName());

    @Inject
    @ConfigProperty(name = "wolpertinger.WolpertingerIdentityStore.admin.name")
    private String              name;
    @Inject
    @ConfigProperty(name = "wolpertinger.WolpertingerIdentityStore.admin.password")
    private String              password;

    @Override
    public CredentialValidationResult validate(Credential credential)
    {
        if (name == null || password == null) {
            LOGGER.severe("Not configured correctly.");
            return NOT_VALIDATED_RESULT;
        }
        if (!(credential instanceof UsernamePasswordCredential)) {
            LOGGER.warning("Unknown credential type: " + credential.getClass().getName());
            return NOT_VALIDATED_RESULT;
        }
        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential)credential;
        String callerName = usernamePasswordCredential.getCaller();
        if (usernamePasswordCredential.compareTo(name, password)) {
            return new CredentialValidationResult(callerName, new HashSet<>(asList(Roles.ADMIN)));
        }
        LOGGER.warning("User '" + usernamePasswordCredential.getCaller() + "' tried to sign in.");
        return INVALID_RESULT;
    }
}