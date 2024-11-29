package com.lpbici.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import static com.lpbici.controller.UsuarioController.getRandomNumber;
public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws Exception {
		Map<String, Object> pathVars = (Map<String, Object>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String strippedServletPath = req.getServletPath();
		if (strippedServletPath.equals("/usuario/migracionTenant")){
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getServletContext());
			UsuarioController usuarioController = context.getBean(UsuarioController.class);
			if (usuarioController.actualizarRolAntesDeMigracion()){
				req.setAttribute("CURRENT_TENANT_IDENTIFIER", usuarioController.configurarTenant(getRandomNumber()));
			}
		}
		if (pathVars.containsKey("tenantid")) {
			req.setAttribute("CURRENT_TENANT_IDENTIFIER", pathVars.get("tenantid"));
		}
		return true;
	}
}