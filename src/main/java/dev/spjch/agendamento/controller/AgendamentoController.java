package dev.spjch.agendamento.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.spjch.agendamento.dto.AgendamentoCreateRequest;
import dev.spjch.agendamento.dto.AgendamentoResponse;
import dev.spjch.agendamento.dto.AgendamentoUpdateRequest;
import dev.spjch.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

	private final AgendamentoService agendamentoService;

	public AgendamentoController(AgendamentoService agendamentoService) {
		this.agendamentoService = agendamentoService;
	}

	@PostMapping
	public AgendamentoResponse criar(@Valid @RequestBody AgendamentoCreateRequest request) {
		return agendamentoService.criar(request);
	}

	@PutMapping("/{id}")
	public AgendamentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoUpdateRequest request) {
		return agendamentoService.atualizar(id, request);
	}

	@PutMapping("/{id}/cancelar")
	public AgendamentoResponse cancelar(@PathVariable Long id) {
		return agendamentoService.cancelar(id);
	}

	@PutMapping("/{id}/concluir")
	public AgendamentoResponse concluir(@PathVariable Long id) {
		return agendamentoService.concluir(id);
	}

	@GetMapping("/{id}")
	public AgendamentoResponse buscarPorId(@PathVariable Long id) {
		return agendamentoService.buscarPorId(id);
	}

}
