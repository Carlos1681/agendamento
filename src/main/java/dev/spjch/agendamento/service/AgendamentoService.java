package dev.spjch.agendamento.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import dev.spjch.agendamento.dto.AgendamentoCreateRequest;
import dev.spjch.agendamento.dto.AgendamentoResponse;
import dev.spjch.agendamento.dto.AgendamentoUpdateRequest;
import dev.spjch.agendamento.mapper.AgendamentoMapper;
import dev.spjch.agendamento.model.Agendamento;
import dev.spjch.agendamento.model.StatusAgendamento;
import dev.spjch.agendamento.repository.AgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Validated
public class AgendamentoService {

	private final AgendamentoRepository agendamentoRepository;

	public AgendamentoService(AgendamentoRepository agendamentoRepository) {
		this.agendamentoRepository = agendamentoRepository;
	}

	@Transactional
	public AgendamentoResponse criar(@Valid AgendamentoCreateRequest request) {

		validarIntervalo(request.dataInicio(), request.dataFim());
		checkConflito(request.usuario(), request.dataInicio(), request.dataFim(), null);

		Agendamento entity = AgendamentoMapper.toEntity(request);
		entity = agendamentoRepository.save(entity);
		return AgendamentoMapper.toResponse(entity);
	}

	@Transactional
	public AgendamentoResponse atualizar(Long id, @Valid AgendamentoUpdateRequest request) {

		Agendamento entity = agendamentoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));
		AgendamentoMapper.merge(entity, request);
		validarIntervalo(request.dataInicio(), request.dataFim());
		checkConflito(entity.getUsuario(), request.dataInicio(), request.dataFim(), entity.getId());

		entity = agendamentoRepository.save(entity);
		return AgendamentoMapper.toResponse(entity);

	}

	@Transactional
	public AgendamentoResponse cancelar(Long id) {
		Agendamento entity = agendamentoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));
		entity.setStatus(StatusAgendamento.CANCELADO);
		entity = agendamentoRepository.save(entity);
		return AgendamentoMapper.toResponse(entity);
	}

	@Transactional
	public AgendamentoResponse concluir(Long id) {
		Agendamento entity = agendamentoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));
		entity.setStatus(StatusAgendamento.CONCLUIDO);
		return AgendamentoMapper.toResponse(entity);
	}

	public AgendamentoResponse buscarPorId(Long id) {
		Agendamento entity = agendamentoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));
		return AgendamentoMapper.toResponse(entity);
	}

	private void validarIntervalo(LocalDateTime inicio, LocalDateTime fim) {
		if (inicio == null || fim == null || inicio.isAfter(fim)) {
			throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
		}
	}

	private void checkConflito(String usuario, LocalDateTime inicio, LocalDateTime fim, Long id) {
		if (agendamentoRepository.existsConflito(usuario, inicio, fim, id)) {
			throw new IllegalArgumentException("Já existe um agendamento nesse intervalo de tempo.");
		}
	}

}
