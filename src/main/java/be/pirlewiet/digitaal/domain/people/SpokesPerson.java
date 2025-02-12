package be.pirlewiet.digitaal.domain.people;

import be.pirlewiet.digitaal.model.*;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Set;

@Component
public class SpokesPerson {
    protected final TemplateEngine templateEngine;

    public SpokesPerson(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String formatIntakeMessageOrganisation(Application application, List<Person> participants, Set<Holiday> holidays, Person recipient ) {
        final String template = "email/to-organisation/intake";
        final Context ctx = new Context();
        ctx.setVariable("holidays", holidays);
        ctx.setVariable("participants",participants);
        return this.templateEngine.process(template, ctx);
    }

    public String formatIntakeMessageTourist(Application application, List<Person> participants, Set<Holiday> holidays, Person applicant ) {
        final String template = "email/to-tourist/intake";
        final Context ctx = new Context();
        ctx.setVariable("applicant", applicant);
        ctx.setVariable("holidays", holidays);
        ctx.setVariable("participants",participants);
        return this.templateEngine.process(template, ctx);
    }

    public String formatEnrollmentStatusUpdateMessageOrganisation(Enrollment enrollment, Person participant, Set<Holiday> holidays, EnrollmentStatus oldStatus, Person applicant  ) {
        final String template = "email/to-organisation/enrollment-status-update";
        final Context ctx = new Context();
        ctx.setVariable( "enrollment", enrollment );
        ctx.setVariable( "participant", participant );
        ctx.setVariable( "holiday", enrollment.getHolidayName() );
        ctx.setVariable( "oldStatus", oldStatus );
        ctx.setVariable( "newStatus", enrollment.getStatus());
        ctx.setVariable( "newStatusComment", enrollment.getStatus() );
        ctx.setVariable( "uuid", enrollment.getUuid() );
        return this.templateEngine.process(template, ctx);
    }

    protected String formatOrganisationIntakeMessagePirlewiet( Application application, List<Person> participants, Set<Holiday> holidays, Person applicant, Organisation organisation ) {
        final String template = "email/to-pirlewiet/intake-organisation";
        final Context ctx = new Context();
        ctx.setVariable("applicant", applicant);
        ctx.setVariable("organisation", organisation);
        ctx.setVariable("holidays", holidays);
        ctx.setVariable("participants",participants);
        return this.templateEngine.process(template, ctx);
    }

    protected String formatReferencedIntakeMessagePirlewiet( Application application, List<Person> participants, Set<Holiday> holidays, Person applicant, Organisation organisation ) {
        final String template = "email/to-pirlewiet/intake-referenced";
        final Context ctx = new Context();
        ctx.setVariable("applicant", applicant);
        ctx.setVariable("organisation", organisation);
        ctx.setVariable("holidays", holidays);
        ctx.setVariable("participants",participants);
        return this.templateEngine.process(template, ctx);
    }

    public String formatCodeRequestMessages(String code ) {
        final String template = "email/to-organisation/code-request";
        final Context ctx = new Context();
        ctx.setVariable("code", code);
        return this.templateEngine.process(template, ctx);
    }

}
