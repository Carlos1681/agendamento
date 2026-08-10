package dev.spjch.agendamento.mapper;

import java.time.LocalDateTime;

import dev.spjch.agendamento.dto.AgendamentoCreateRequest;
import dev.spjch.agendamento.dto.AgendamentoResponse;
import dev.spjch.agendamento.dto.AgendamentoUpdateRequest;
import dev.spjch.agendamento.model.Agendamento;
import dev.spjch.agendamento.model.StatusAgendamento;

public class AgendamentoMapper {

	public static Agendamento toEntity(AgendamentoCreateRequest request) {

		return Agendamento.builder().titulo(request.titulo()).descricao(request.descricao())
				.dataInicio(request.dataInicio()).dataFim(request.dataFim()).usuario(request.usuario())
				.status(StatusAgendamento.AGENDADO).criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now())
				.build();
	}

	public static void merge(Agendamento agendamento, AgendamentoUpdateRequest request) {
		if (request.titulo() != null) {
			agendamento.setTitulo(request.titulo());
		}
		if (request.descricao() != null) {
			agendamento.setDescricao(request.descricao());
		}
		if (request.dataInicio() != null) {
			agendamento.setDataInicio(request.dataInicio());
		}
		if (request.dataFim() != null) {
			agendamento.setDataFim(request.dataFim());
		}
	}

	public static AgendamentoResponse toResponse(Agendamento agendamento) {

		return new AgendamentoResponse(agendamento.getId(), agendamento.getTitulo(), agendamento.getDescricao(),
				agendamento.getDataInicio(), agendamento.getDataFim(), agendamento.getStatus(),
				agendamento.getUsuario(), agendamento.getCriadoEm(), agendamento.getAtualizadoEm());
	}

}
