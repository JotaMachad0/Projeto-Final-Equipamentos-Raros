package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringResponseDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.MapperExpectedHiring;
import br.com.raroacademy.demo.domain.entities.ExpectedHiringEntity;
import br.com.raroacademy.demo.domain.enums.ExpectedHiringStatus;
import br.com.raroacademy.demo.exception.ExpectedHiringAlreadyExistsException;
import br.com.raroacademy.demo.exception.InvalidStatusException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.ExpectedHiringRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpectedHiringService {

    private final ExpectedHiringRepository expectedHiringRepository;
    private final MapperExpectedHiring mapperExpectedHiring;
    private final I18nUtil i18nUtil;

    @Transactional
    public ExpectedHiringResponseDTO create(@Valid ExpectedHiringRequestDTO request) {
        var existing = expectedHiringRepository
                .findByExpectedHireDateAndPositionIgnoreCaseAndRegion(
                        request.expectedHireDate(), request.position(), request.region());

        if (existing.isPresent()) {
            ExpectedHiringEntity e = existing.get();

            String localizedStatus = i18nUtil.getMessage(
                    "expected.hiring.status." + e.getExpectedHiringStatus().name()
            );
            String message = i18nUtil.getMessage(
                    "expected.hiring.already.exists", e.getId(), localizedStatus, e.getEquipmentRequirements()
            );

            throw new ExpectedHiringAlreadyExistsException(message);
        }

        var expectedHiring = mapperExpectedHiring.toExpectedHiring(request);
        var saved = expectedHiringRepository.save(expectedHiring);
        return mapperExpectedHiring.toExpectedHiringResponseDTO(saved);
    }

    @Transactional
    public ExpectedHiringResponseDTO getExpectedHiringById(Long id) {
        var expectedHiring = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("expected.hiring.not.found")));

        updateStatusIfExpired(expectedHiring);

        return mapperExpectedHiring.toExpectedHiringResponseDTO(expectedHiring);
    }

    @Transactional
    public ExpectedHiringResponseDTO update(Long id, @Valid ExpectedHiringRequestDTO request) {
        var existing = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("expected.hiring.not.found")));

        var duplicate = expectedHiringRepository
                .findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                        request.expectedHireDate(), request.position(), request.region(), id);

        if (duplicate.isPresent()) {
            var d = duplicate.get();
            throw new ExpectedHiringAlreadyExistsException(
                    i18nUtil.getMessage(
                            "expected.hiring.already.exists",
                            d.getId())
            );
        }

        var updated = mapperExpectedHiring.toApplyUpdates(existing, request);

        if (existing.equals(updated)) {
            log.info(i18nUtil.getMessage("expected.hiring.unchanged", id));
            return mapperExpectedHiring.toExpectedHiringResponseDTO(existing);
        }

        return mapperExpectedHiring.toExpectedHiringResponseDTO(
                expectedHiringRepository.save(updated)
        );
    }

    @Transactional
    public void delete(Long id) {
        var expectedHiring = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("expected.hiring.not.found")));
        expectedHiringRepository.delete(expectedHiring);
    }

    @Transactional
    public List<ExpectedHiringResponseDTO> getAllExpectedHirings() {
        var expectedHiringList = expectedHiringRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        expectedHiringList.forEach(this::updateStatusIfExpired);

        return mapperExpectedHiring.toExpectedHiringList(expectedHiringList);
    }

    @Transactional
    public void markAsProcessed(Long id) {
        var entity = expectedHiringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("expected.hiring.not.found")));

        if (entity.getExpectedHiringStatus() != ExpectedHiringStatus.CREATED) {
            throw new InvalidStatusException(i18nUtil.getMessage("expected.hiring.invalid.status"));
        }

        entity.setExpectedHiringStatus(ExpectedHiringStatus.PROCESSED);
        expectedHiringRepository.save(entity);
    }

    private void updateStatusIfExpired(ExpectedHiringEntity entity) {
        var today = LocalDate.now();
        ExpectedHiringStatus newExpectedHiringStatus = null;

        switch (entity.getExpectedHiringStatus()) {
            case CREATED -> {
                if (entity.getExpectedHireDate().isBefore(today)) {
                    newExpectedHiringStatus = ExpectedHiringStatus.EXPIRED;
                }
            }
            case PROCESSED -> {
                if (entity.getExpectedHireDate().isBefore(today)) {
                    newExpectedHiringStatus = ExpectedHiringStatus.CONCLUDED;
                }
            }
            default -> {
            }
        }

        if (newExpectedHiringStatus != null) {
            entity.setExpectedHiringStatus(newExpectedHiringStatus);
            expectedHiringRepository.save(entity);
        }
    }
}
