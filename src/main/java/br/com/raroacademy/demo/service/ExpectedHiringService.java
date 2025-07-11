package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringResponseDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.MapperExpectedHiring;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.ExpectedHiringRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpectedHiringService {

    private final ExpectedHiringRepository expectedHiringRepository;
    private final MapperExpectedHiring mapperExpectedHiring;

    @Transactional
    public ExpectedHiringResponseDTO create(@Valid ExpectedHiringRequestDTO request) {
        var expectedHiring = mapperExpectedHiring.toExpectedHiring(request);
        var saved = expectedHiringRepository.save(expectedHiring);
        return mapperExpectedHiring.toExpectedHiringResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public ExpectedHiringResponseDTO getExpectedHiringById(Long id) {
        var expectedHiring = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Previsão de contratação não encontrada"));
        return mapperExpectedHiring.toExpectedHiringResponseDTO(expectedHiring);
    }

    @Transactional
    public ExpectedHiringResponseDTO update(Long id, @Valid ExpectedHiringRequestDTO request) {
        var existing = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Previsão de contratação não encontrada"));

        var updated = mapperExpectedHiring.toApplyUpdates(existing, request);

        if (existing.equals(updated)) {
            return mapperExpectedHiring.toExpectedHiringResponseDTO(existing);
        }

        return mapperExpectedHiring.toExpectedHiringResponseDTO(
                expectedHiringRepository.save(updated)
        );
    }

    @Transactional
    public void delete(Long id) {
        var expectedHiring = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Previsão de contratação não encontrada"));
        expectedHiringRepository.delete(expectedHiring);
    }

    @Transactional(readOnly = true)
    public List<ExpectedHiringResponseDTO> getAllExpectedHirings() {
        var expectedHiringList = expectedHiringRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return mapperExpectedHiring.toExpectedHiringList(expectedHiringList);
    }
}
