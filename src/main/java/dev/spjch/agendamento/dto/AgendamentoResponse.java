package dev.spjch.agendamento.dto;

import java.time.LocalDateTime;

import dev.spjch.agendamento.model.StatusAgendamento;

public record AgendamentoResponse(Long id, String titulo, String descricao, LocalDateTime dataInicio,
		LocalDateTime dataFim, StatusAgendamento status, String usuario, LocalDateTime criadoEm,
		LocalDateTime atualizadoEm) {

}
