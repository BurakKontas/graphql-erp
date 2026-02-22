package tr.kontas.erp.app.notification.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.notification.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.notification.application.config.*;
import tr.kontas.erp.notification.application.inapp.*;
import tr.kontas.erp.notification.application.preference.*;
import tr.kontas.erp.notification.application.template.*;
import tr.kontas.erp.notification.application.tenantconfig.*;
import tr.kontas.erp.notification.application.webhook.*;
import tr.kontas.erp.notification.domain.DeliveryTiming;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.config.NotificationConfig;
import tr.kontas.erp.notification.domain.config.NotificationConfigId;
import tr.kontas.erp.notification.domain.preference.NotificationPreference;
import tr.kontas.erp.notification.domain.template.NotificationTemplate;
import tr.kontas.erp.notification.domain.template.NotificationTemplateId;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfig;
import tr.kontas.erp.notification.domain.webhook.WebhookConfig;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigId;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class NotificationGraphql {

    private final CreateTenantNotificationConfigUseCase createTenantNotificationConfigUseCase;
    private final GetTenantNotificationConfigUseCase getTenantNotificationConfigUseCase;
    private final UpdateTenantNotificationConfigUseCase updateTenantNotificationConfigUseCase;
    private final CreateNotificationConfigUseCase createNotificationConfigUseCase;
    private final GetNotificationConfigByIdUseCase getNotificationConfigByIdUseCase;
    private final GetNotificationConfigsByCompanyUseCase getNotificationConfigsByCompanyUseCase;
    private final CreateNotificationTemplateUseCase createNotificationTemplateUseCase;
    private final GetNotificationTemplateByIdUseCase getNotificationTemplateByIdUseCase;
    private final GetNotificationTemplatesUseCase getNotificationTemplatesUseCase;
    private final UpdateNotificationTemplateUseCase updateNotificationTemplateUseCase;
    private final CreateNotificationPreferenceUseCase createNotificationPreferenceUseCase;
    private final GetNotificationPreferencesByUserUseCase getNotificationPreferencesByUserUseCase;
    private final UpdateNotificationPreferenceUseCase updateNotificationPreferenceUseCase;
    private final CreateWebhookConfigUseCase createWebhookConfigUseCase;
    private final GetWebhookConfigByIdUseCase getWebhookConfigByIdUseCase;
    private final GetWebhookConfigsByCompanyUseCase getWebhookConfigsByCompanyUseCase;
    private final GetInAppNotificationsByUserUseCase getInAppNotificationsByUserUseCase;
    private final MarkNotificationReadUseCase markNotificationReadUseCase;
    private final MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase;

    // ─── TenantNotificationConfig ───

    @DgsQuery
    public TenantNotificationConfigPayload tenantNotificationConfig() {
        TenantNotificationConfig config = getTenantNotificationConfigUseCase.execute();
        return toTenantConfigPayload(config);
    }

    @DgsMutation
    public TenantNotificationConfigPayload createTenantNotificationConfig() {
        createTenantNotificationConfigUseCase.execute(new CreateTenantNotificationConfigCommand());
        return toTenantConfigPayload(getTenantNotificationConfigUseCase.execute());
    }

    @DgsMutation
    public TenantNotificationConfigPayload updateTenantNotificationConfig(@InputArgument("input") UpdateTenantNotificationConfigInput input) {
        Instant disabledUntil = input.getDisabledUntil() != null ? Instant.parse(input.getDisabledUntil()) : null;
        updateTenantNotificationConfigUseCase.execute(new UpdateTenantNotificationConfigCommand(
                input.isAllNotificationsEnabled(),
                input.getDisabledReason(),
                disabledUntil
        ));
        return toTenantConfigPayload(getTenantNotificationConfigUseCase.execute());
    }

    // ─── NotificationConfig ───

    @DgsQuery
    public NotificationConfigPayload notificationConfig(@InputArgument("id") String id) {
        NotificationConfig config = getNotificationConfigByIdUseCase.execute(NotificationConfigId.of(id));
        return toConfigPayload(config);
    }

    @DgsQuery
    public List<NotificationConfigPayload> notificationConfigs(@InputArgument("companyId") String companyId) {
        return getNotificationConfigsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(NotificationGraphql::toConfigPayload).toList();
    }

    @DgsMutation
    public NotificationConfigPayload createNotificationConfig(@InputArgument("input") CreateNotificationConfigInput input) {
        CreateNotificationConfigCommand cmd = new CreateNotificationConfigCommand(
                CompanyId.of(input.getCompanyId()),
                input.getEventType(),
                input.getNotificationKey(),
                input.getDescription(),
                input.getEnabledChannels() != null ? new HashSet<>(input.getEnabledChannels()) : null,
                input.getDeliveryTiming() != null ? DeliveryTiming.valueOf(input.getDeliveryTiming()) : DeliveryTiming.IMMEDIATE,
                input.getCronExpression(),
                input.getRecipientRoles() != null ? new HashSet<>(input.getRecipientRoles()) : null,
                input.getUserOverridable() != null && input.getUserOverridable()
        );
        NotificationConfigId id = createNotificationConfigUseCase.execute(cmd);
        return toConfigPayload(getNotificationConfigByIdUseCase.execute(id));
    }

    // ─── NotificationTemplate ───

    @DgsQuery
    public NotificationTemplatePayload notificationTemplate(@InputArgument("id") String id) {
        return toTemplatePayload(getNotificationTemplateByIdUseCase.execute(NotificationTemplateId.of(id)));
    }

    @DgsQuery
    public List<NotificationTemplatePayload> notificationTemplates() {
        return getNotificationTemplatesUseCase.execute()
                .stream().map(NotificationGraphql::toTemplatePayload).toList();
    }

    @DgsMutation
    public NotificationTemplatePayload createNotificationTemplate(@InputArgument("input") CreateNotificationTemplateInput input) {
        CreateNotificationTemplateCommand cmd = new CreateNotificationTemplateCommand(
                input.getNotificationKey(),
                NotificationChannel.valueOf(input.getChannel()),
                input.getLocale(),
                input.getSubject(),
                input.getBodyTemplate(),
                input.getSystemTemplate() != null && input.getSystemTemplate()
        );
        NotificationTemplateId id = createNotificationTemplateUseCase.execute(cmd);
        return toTemplatePayload(getNotificationTemplateByIdUseCase.execute(id));
    }

    @DgsMutation
    public NotificationTemplatePayload updateNotificationTemplate(@InputArgument("input") UpdateNotificationTemplateInput input) {
        updateNotificationTemplateUseCase.execute(new UpdateNotificationTemplateCommand(
                input.getTemplateId(),
                input.getSubject(),
                input.getBodyTemplate()
        ));
        return toTemplatePayload(getNotificationTemplateByIdUseCase.execute(NotificationTemplateId.of(input.getTemplateId())));
    }

    // ─── NotificationPreference ───

    @DgsQuery
    public List<NotificationPreferencePayload> notificationPreferences(@InputArgument("userId") String userId) {
        return getNotificationPreferencesByUserUseCase.execute(userId)
                .stream().map(NotificationGraphql::toPreferencePayload).toList();
    }

    @DgsMutation
    public NotificationPreferencePayload createNotificationPreference(@InputArgument("input") CreateNotificationPreferenceInput input) {
        CreateNotificationPreferenceCommand cmd = new CreateNotificationPreferenceCommand(
                input.getUserId(),
                input.getNotificationKey(),
                input.getDisabledChannels() != null ? new HashSet<>(input.getDisabledChannels()) : null,
                input.getPreferredLocale()
        );
        createNotificationPreferenceUseCase.execute(cmd);
        List<NotificationPreference> prefs = getNotificationPreferencesByUserUseCase.execute(input.getUserId());
        return prefs.stream()
                .filter(p -> p.getNotificationKey().equals(input.getNotificationKey()))
                .map(NotificationGraphql::toPreferencePayload)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification preference not found"));
    }

    @DgsMutation
    public NotificationPreferencePayload updateNotificationPreference(@InputArgument("input") UpdateNotificationPreferenceInput input) {
        updateNotificationPreferenceUseCase.execute(new UpdateNotificationPreferenceCommand(
                input.getPreferenceId(),
                input.getDisabledChannels() != null ? new HashSet<>(input.getDisabledChannels()) : null,
                input.getPreferredLocale()
        ));
        List<NotificationPreference> prefs = getNotificationPreferencesByUserUseCase.execute(null);
        return prefs.stream()
                .filter(p -> p.getId().asUUID().toString().equals(input.getPreferenceId()))
                .map(NotificationGraphql::toPreferencePayload)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification preference not found: " + input.getPreferenceId()));
    }

    // ─── WebhookConfig ───

    @DgsQuery
    public WebhookConfigPayload webhookConfig(@InputArgument("id") String id) {
        return toWebhookPayload(getWebhookConfigByIdUseCase.execute(WebhookConfigId.of(id)));
    }

    @DgsQuery
    public List<WebhookConfigPayload> webhookConfigs(@InputArgument("companyId") String companyId) {
        return getWebhookConfigsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(NotificationGraphql::toWebhookPayload).toList();
    }

    @DgsMutation
    public WebhookConfigPayload createWebhookConfig(@InputArgument("input") CreateWebhookConfigInput input) {
        CreateWebhookConfigCommand cmd = new CreateWebhookConfigCommand(
                CompanyId.of(input.getCompanyId()),
                input.getTargetUrl(),
                input.getSecretKey(),
                input.getEventTypes()
        );
        WebhookConfigId id = createWebhookConfigUseCase.execute(cmd);
        return toWebhookPayload(getWebhookConfigByIdUseCase.execute(id));
    }

    // ─── InAppNotification ───

    @DgsQuery
    public List<InAppNotificationPayload> inAppNotifications(@InputArgument("userId") String userId) {
        return getInAppNotificationsByUserUseCase.executeGet(userId)
                .stream().map(NotificationGraphql::toInAppPayload).toList();
    }

    @DgsMutation
    public boolean markNotificationRead(@InputArgument("notificationId") String notificationId) {
        markNotificationReadUseCase.execute(notificationId);
        return true;
    }

    @DgsMutation
    public boolean markAllNotificationsRead(@InputArgument("userId") String userId) {
        markAllNotificationsReadUseCase.markAllRead(userId);
        return true;
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "NotificationConfigPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        NotificationConfigPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCompanyId());
    }

    // ─── Mapping helpers ───

    private static TenantNotificationConfigPayload toTenantConfigPayload(TenantNotificationConfig c) {
        return new TenantNotificationConfigPayload(
                c.getId().asUUID().toString(),
                c.isAllNotificationsEnabled(),
                c.getDisabledChannels() != null ? c.getDisabledChannels().stream().map(Enum::name).toList() : List.of(),
                c.getDisabledKeys() != null ? c.getDisabledKeys().stream().toList() : List.of(),
                c.getDisabledReason(),
                c.getDisabledBy(),
                c.getDisabledAt() != null ? c.getDisabledAt().toString() : null,
                c.getDisabledUntil() != null ? c.getDisabledUntil().toString() : null
        );
    }

    public static NotificationConfigPayload toConfigPayload(NotificationConfig c) {
        return new NotificationConfigPayload(
                c.getId().asUUID().toString(),
                c.getCompanyId().asUUID().toString(),
                c.getEventType(),
                c.getNotificationKey(),
                c.getDescription(),
                c.getEnabledChannels() != null ? c.getEnabledChannels().stream().map(Enum::name).toList() : List.of(),
                c.getDeliveryTiming().name(),
                c.getCronExpression(),
                c.getRecipientRoles() != null ? c.getRecipientRoles().stream().toList() : List.of(),
                c.isUserOverridable(),
                c.isActive()
        );
    }

    public static NotificationTemplatePayload toTemplatePayload(NotificationTemplate t) {
        return new NotificationTemplatePayload(
                t.getId().asUUID().toString(),
                t.getNotificationKey(),
                t.getChannel().name(),
                t.getLocale(),
                t.getSubject(),
                t.getBodyTemplate(),
                t.isSystemTemplate(),
                t.isActive()
        );
    }

    public static NotificationPreferencePayload toPreferencePayload(NotificationPreference p) {
        return new NotificationPreferencePayload(
                p.getId().asUUID().toString(),
                p.getUserId(),
                p.getNotificationKey(),
                p.getDisabledChannels() != null ? p.getDisabledChannels().stream().map(Enum::name).toList() : List.of(),
                p.getPreferredLocale()
        );
    }

    public static WebhookConfigPayload toWebhookPayload(WebhookConfig w) {
        return new WebhookConfigPayload(
                w.getId().asUUID().toString(),
                w.getCompanyId().asUUID().toString(),
                w.getTargetUrl(),
                w.getEventTypes(),
                w.getStatus().name(),
                w.getConsecutiveFailures(),
                w.getLastSuccessAt() != null ? w.getLastSuccessAt().toString() : null
        );
    }

    private static InAppNotificationPayload toInAppPayload(InAppNotificationResult r) {
        return new InAppNotificationPayload(
                r.id(),
                r.userId(),
                r.notificationKey(),
                r.title(),
                r.body(),
                r.actionUrl(),
                r.read(),
                r.readAt() != null ? r.readAt().toString() : null,
                r.createdAt() != null ? r.createdAt().toString() : null
        );
    }
}

