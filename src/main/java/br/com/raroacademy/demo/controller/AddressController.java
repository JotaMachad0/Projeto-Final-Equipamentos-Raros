package br.com.raroacademy.demo.controller;
import br.com.raroacademy.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService service;

//    @PostMapping
//    public ResponseEntity<AddressResponseDTO> createAddress(@RequestBody @Valid AddressRequestDTO dto) {
//        return ResponseEntity.ok(service.save(dto));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<AddressResponseDTO> getAddress(@PathVariable Long id) {
//        return ResponseEntity.ok(service.findById(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
//        return ResponseEntity.ok(service.findAll());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
//        service.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
}
