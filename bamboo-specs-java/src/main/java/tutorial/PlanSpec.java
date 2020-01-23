import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.BambooOid;
import com.atlassian.bamboo.specs.api.builders.notification.Notification;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.notification.PlanFailedNotification;
import com.atlassian.bamboo.specs.builders.notification.ResponsibleRecipient;
import com.atlassian.bamboo.specs.builders.notification.WatchersRecipient;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.builders.trigger.RepositoryPollingTrigger;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.util.BambooServer;

@BambooSpec
public class PlanSpec {

    public Plan plan() {
        final Plan plan = new Plan(new Project()
                .oid(new BambooOid("14v244x06mpdu"))
                .key(new BambooKey("FAST"))
                .name("FAST"),
            "Demo FAST testing plan",
            new BambooKey("FAST"))
            .oid(new BambooOid("14usexbmyswzm"))
            .pluginConfigurations(new ConcurrentBuilds())
            .stages(new Stage("building")
                    .jobs(new Job("build",
                            new BambooKey("BLD"))
                            .tasks(new VcsCheckoutTask()
                                    .checkoutItems(new CheckoutItem().defaultRepository()),
                                new ScriptTask()
                                    .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                    .inlineBody("docker-compose build"))),
                new Stage("testing")
                    .jobs(new Job("tests",
                            new BambooKey("TST"))
                            .tasks(new VcsCheckoutTask()
                                    .checkoutItems(new CheckoutItem().defaultRepository()),
                                new ScriptTask()
                                    .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                    .inlineBody("docker-compose up -d dvwa fast selenium"),
                                new ScriptTask()
                                    .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                    .inlineBody("docker-compose run --service-ports test"),
                                new ScriptTask()
                                    .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                    .inlineBody("docker-compose run -e CI_MODE=testing -e TEST_RUN_URI=http://dvwa:80 fast"))),
                new Stage("cleaning")
                    .finalStage(true)
                    .jobs(new Job("cleanup",
                            new BambooKey("CLN"))
                            .tasks(new VcsCheckoutTask()
                                    .checkoutItems(new CheckoutItem().defaultRepository()),
                                new ScriptTask()
                                    .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                    .inlineBody("docker-compose down"))))
            .linkedRepositories("fast-example-azure-dvwa-integration")

            .triggers(new RepositoryPollingTrigger())
            .variables(new Variable("secret_token",
                    "BAMSCRT@0@0@BD+8NIfWD4AtzHDwm3A5TF9YQOacU1npnP/2sw7MdoR5JIuBtMbsvvrJS1fI8AIkBZ2tFRKfSYiQbN0hrf9APV8JUDDTKu5CwGlZxfseou0="))
            .planBranchManagement(new PlanBranchManagement()
                    .createForVcsBranch()
                    .delete(new BranchCleanup()
                        .whenRemovedFromRepositoryAfterDays(1)
                        .whenInactiveInRepositoryAfterDays(30))
                    .notificationLikeParentPlan())
            .notifications(new Notification()
                    .type(new PlanFailedNotification())
                    .recipients(new ResponsibleRecipient(),
                        new WatchersRecipient()))
            .forceStopHungBuilds();
        return plan;
    }

    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier("FAST", "FAST"))
            .permissions(new Permissions()
                    .loggedInUserPermissions(PermissionType.VIEW)
                    .anonymousUserPermissionView());
        return planPermission;
    }

    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
        BambooServer bambooServer = new BambooServer("http://127.0.0.1:8085");
        final PlanSpec planSpec = new PlanSpec();

        final Plan plan = planSpec.plan();
        bambooServer.publish(plan);

        final PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);
    }
}
