package com.lpbici.util;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Establece el identificador del tenant actual en el contexto.
     *
     * @param tenant el identificador del tenant
     */
    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    /**
     * Obtiene el identificador del tenant actual del contexto.
     *
     * @return el identificador del tenant o null si no está configurado
     */
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Limpia el identificador del tenant actual del contexto.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}