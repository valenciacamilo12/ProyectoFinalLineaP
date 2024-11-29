    package com.lpbici.controller;

    import com.lpbici.entity.Rol;
    import com.lpbici.util.TenantContext;
    import com.lpbici.entity.Usuario;
    import com.lpbici.security.service.UsuarioPrincipal;
    import enums.RolNombre;
    import com.lpbici.service.RolService;
    import com.lpbici.service.UsuarioService;
    import org.apache.commons.lang3.StringUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.ModelAndView;

    import java.util.HashSet;
    import java.util.List;
    import java.util.Random;
    import java.util.Set;

    @Controller
    @RequestMapping("/usuario")
    public class UsuarioController {

        @Autowired
        UsuarioService usuarioService;

        @Autowired
        RolService rolService;

        @Autowired
        PasswordEncoder passwordEncoder;

        @RequestMapping(value = "/registro", method = RequestMethod.GET)
        public String registro(){
            return "registro";
        }

        @RequestMapping(value = "/registrar", method = RequestMethod.POST)
        public ModelAndView registrar(String nombreUsuario, String password){
            ModelAndView mv = new ModelAndView();
            if(StringUtils.isBlank(nombreUsuario)){
                mv.setViewName("/registro");
                mv.addObject("error", "el nombre no puede estar vacío");
                return mv;
            }
            if(StringUtils.isBlank(password)){
                mv.setViewName("/registro");
                mv.addObject("error", "la contraseña no puede estar vacía");
                return mv;
            }
            if(usuarioService.existsByNombreusuario(nombreUsuario)){
                mv.setViewName("/registro");
                mv.addObject("error", "ese nombre de usuario ya existe");
                return mv;
            }
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setPassword(passwordEncoder.encode(password));
            Rol rolUser = rolService.getByRolNombre(RolNombre.ROL_VISIT).get();
            Set<Rol> roles = new HashSet<>();
            roles.add(rolUser);
            usuario.setRoles(roles);
            usuarioService.save(usuario);
            mv.setViewName("/login");
            mv.addObject("registroOK", "Cuenta creada, " + usuario.getNombreUsuario() + ", ya puedes iniciar sesión");
            return mv;
        }

        @RequestMapping(value = "lista", method = RequestMethod.GET)
        public ModelAndView list(){
            ModelAndView mv = new ModelAndView();
            mv.setViewName("/usuario/lista");
            List<Usuario> usuarios = usuarioService.lista();
            mv.addObject("usuarios", usuarios);
            return mv;
        }

        @RequestMapping(value = "/actualizarRol", method = RequestMethod.POST)
        public ModelAndView actualizarRol(@RequestParam String rol) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !authentication.getPrincipal().equals("anonymousUser")) {
                UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
                Usuario usuario = usuarioService.getByNombreUsuario(usuarioPrincipal.getUsername()).get();
                Rol rolUser = rolService.getByRolNombre(RolNombre.valueOf(rol)).orElseThrow(() -> new IllegalArgumentException("Rol no válido: " + rol));
                try {
                    Set<Rol> roles = new HashSet<>();
                    roles.add(rolUser);
                    usuario.setRoles(roles);
                    usuarioService.save(usuario);
                } catch (IllegalArgumentException e) {
                    return new ModelAndView("error").addObject("message", e.getMessage());
                }
            }
            return new ModelAndView("redirect:/producto/compraexitosa");
        }

        @RequestMapping(value = "/migracionTenant", method = RequestMethod.POST)
        public ModelAndView migracionTenant(@RequestParam String rol) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();

            if(!usuarioService.existsByNombreusuario(usuarioPrincipal.getUsername())){
                Usuario usuarioMigracion = new Usuario();
                usuarioMigracion.setNombreUsuario(usuarioPrincipal.getUsername());
                usuarioMigracion.setPassword(passwordEncoder.encode(usuarioPrincipal.getPassword()));
                Rol rolUserMigracion = rolService.getByRolNombre(RolNombre.ROL_SPINSENSEI).get();
                Set<Rol> roles = new HashSet<>();
                roles.add(rolUserMigracion);
                usuarioMigracion.setRoles(roles);
                usuarioService.save(usuarioMigracion);
                return new ModelAndView("redirect:/producto/compraexitosa");
            }
            return new ModelAndView("redirect:/producto/compraexitosa");
        }

        public boolean actualizarRolAntesDeMigracion() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !authentication.getPrincipal().equals("anonymousUser")) {
                UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
                Usuario usuario = usuarioService.getByNombreUsuario(usuarioPrincipal.getUsername()).get();
                Rol rolUser = rolService.getByRolNombre(RolNombre.ROL_SPINSENSEI).get();
                try {
                    Set<Rol> roles = new HashSet<>();
                    roles.add(rolUser);
                    usuario.setRoles(roles);
                    usuarioService.save(usuario);
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
            return true;
        }
        public String configurarTenant(int tenantId) {
            String tenantIdentifier;
            switch (tenantId) {
                case 1:
                    tenantIdentifier = "tenant_1";
                    break;
                case 2:
                    tenantIdentifier = "tenant_2";
                    break;
                case 3:
                    tenantIdentifier = "tenant_3";
                    break;
                default:
                    tenantIdentifier = "monolito";
            }
            return tenantIdentifier;
        }


        public static int getRandomNumber() {
            Random random = new Random();
            return random.nextInt(3) + 1;
        }
    }
