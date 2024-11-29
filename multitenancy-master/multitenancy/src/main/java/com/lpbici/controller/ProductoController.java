package com.lpbici.controller;

import com.lpbici.entity.Producto;
import com.lpbici.service.ProductoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    ProductoService productoService;
    @RequestMapping(value = "/categorias", method = RequestMethod.GET)
    public String catergorias(){
        return "producto/categorias";
    }

    @RequestMapping(value = "/compraexitosa", method = RequestMethod.GET)
    public String compraExitosa(){
        return "producto/compraexitosa";
    }

    @RequestMapping(value = "/menuproducto", method = RequestMethod.GET)
    public String menuProducto(){return "producto/menuproducto";
    }
    @RequestMapping(value = "/foro", method = RequestMethod.GET)
    public String foro(){return "producto/foro";
    }
    @RequestMapping(value = "/trafico", method = RequestMethod.GET)
    public String trafico(){return "producto/trafico";
    }

    @RequestMapping(value = "/monitoreorendimiento", method = RequestMethod.GET)
    public String monitoreorendimiento(){return "producto/monitoreorendimiento";
    }
    @RequestMapping(value = "/monitoreobienestar", method = RequestMethod.GET)
    public String monitoreobienestar(){return "producto/monitoreobienestar";
    }

    @RequestMapping(value = "lista", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/producto/lista");
        List<Producto> productos = productoService.list();
        mv.addObject("productos", productos);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "nuevo", method = RequestMethod.GET)
    public String nuevo(){
        return "producto/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public ModelAndView crear(@RequestParam String nombre, @RequestParam float precio){
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isBlank(nombre)){
            mv.setViewName("producto/nuevo");
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
        if(precio <1 ){
            mv.setViewName("producto/nuevo");
            mv.addObject("error", "el precio debe ser mayor que cero");
            return mv;
        }
        if(productoService.existsByNombre(nombre)){
            mv.setViewName("producto/nuevo");
            mv.addObject("error", "ese nombre ya existe");
            return mv;
        }
        Producto producto = new Producto(nombre, precio);
        productoService.save(producto);
        mv.setViewName("redirect:/producto/lista");
        return mv;
    }
    @RequestMapping(value = "/detalle/{id}", method = RequestMethod.GET)
    public ModelAndView detalle(@PathVariable("id") int id){
        if(!productoService.existsById(id))
            return new ModelAndView("redirect:/producto/lista");
        Producto producto = productoService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/producto/detalle");
        mv.addObject("producto", producto);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") int id){
        if(!productoService.existsById(id))
            return new ModelAndView("redirect:/producto/lista");
        Producto producto = productoService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/producto/editar");
        mv.addObject("producto", producto);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/actualizar", method = RequestMethod.POST)
    public ModelAndView actualizar(@RequestParam int id, @RequestParam String nombre, @RequestParam float precio){
        if(!productoService.existsById(id))
            return new ModelAndView("redirect:/producto/lista");
        ModelAndView mv = new ModelAndView();
        Producto producto = productoService.getOne(id).get();
        if(StringUtils.isBlank(nombre)){
            mv.setViewName("producto/editar");
            mv.addObject("producto", producto);
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
        if(precio <1 ){
            mv.setViewName("producto/editar");
            mv.addObject("error", "el precio debe ser mayor que cero");
            mv.addObject("producto", producto);
            return mv;
        }
        if(productoService.existsByNombre(nombre) && productoService.getByNombre(nombre).get().getId() != id){
            mv.setViewName("producto/editar");
            mv.addObject("error", "ese nombre ya existe");
            mv.addObject("producto", producto);
            return mv;
        }

        producto.setNombre(nombre);
        producto.setPrecio(precio);
        productoService.save(producto);
        return new ModelAndView("redirect:/producto/lista");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/borrar/{id}", method = RequestMethod.GET)
    public ModelAndView borrar(@PathVariable("id")int id){
        if(productoService.existsById(id)){
            productoService.delete(id);
            return new ModelAndView("redirect:/producto/lista");
        }
        return null;
    }


}
