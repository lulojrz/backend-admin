package com.example.back_admin.Controller;


import com.example.back_admin.Model.Categoria;
import com.example.back_admin.Model.Producto;
import com.example.back_admin.Model.Producto_variantes;
import com.example.back_admin.Repository.CategoriasRepository;
import com.example.back_admin.Repository.ProductoRepository;
import com.example.back_admin.Repository.Producto_variantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private Producto_variantesRepository productoVariantesRepository;
    @Autowired
    private CategoriasRepository categoriasRepository;

    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    @PostMapping
    public Producto agregarProducto(@RequestBody Producto producto_nuevo){
        productoRepository.save(producto_nuevo);
        return producto_nuevo;

    }
    @PostMapping("/variante")
    public Producto_variantes agregarVariante(@RequestBody Producto_variantes variante){
        productoVariantesRepository.save(variante);
        return variante;

    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id){
        productoRepository.deleteById(id);

    }
    @GetMapping("/{id}")
    public List<Producto_variantes> listar_variantes(@PathVariable Long id){
        return productoVariantesRepository.findByProductoId(id);
    }
    @PutMapping("/{id}")
    public Producto editarProducto(@PathVariable Long id,@RequestBody Producto producto_editado){
         return productoRepository.findById(id).map(ProductoExistente ->{
                ProductoExistente.setNombre(producto_editado.getNombre());
                ProductoExistente.setDescripcion(producto_editado.getDescripcion());
                ProductoExistente.setMarca(producto_editado.getMarca());
                ProductoExistente.setPrecioBase(producto_editado.getPrecioBase());
                ProductoExistente.setCategoria(producto_editado.getCategoria());
                ProductoExistente.setPortada(producto_editado.getPortada());
                ProductoExistente.setImagen_principal(producto_editado.getImagen_principal());
                ProductoExistente.setIs_active(producto_editado.getIs_active());
                ProductoExistente.setCreatedAt(producto_editado.getCreatedAt());
                return productoRepository.save(ProductoExistente);

         })
                 .orElse( null);
    }

    @PutMapping("/variante/{id}")
    public Producto_variantes editarVariante(@PathVariable Long id, @RequestBody Producto_variantes nueva_variante){
        return productoVariantesRepository.findById(id).map(
                Productoexistente ->{
                    Productoexistente.setProducto(nueva_variante.getProducto());
                    Productoexistente.setTalla(nueva_variante.getTalla());
                    Productoexistente.setColor(nueva_variante.getColor());
                    Productoexistente.setStock(nueva_variante.getStock());
                    Productoexistente.setSku(nueva_variante.getSku());
                    Productoexistente.setImagen_especifica(nueva_variante.getImagen_especifica());
                    Productoexistente.setPrecioEspecifico(nueva_variante.getPrecioEspecifico());

                    return productoVariantesRepository.save(Productoexistente);

                }
        ).orElse((null));
    }

    @GetMapping("categorias")
    public List<Categoria> obtenerCategorias(){
        return categoriasRepository.findAll();
    }

}
