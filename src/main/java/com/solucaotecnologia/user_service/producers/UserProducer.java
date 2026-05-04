package com.solucaotecnologia.user_service.producers;

import com.solucaotecnologia.user_service.dtos.EmailDto;
import com.solucaotecnologia.user_service.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;


    public void publishMessageEmail(UserModel userModel) {
        var emailDto = new EmailDto();
        emailDto.setId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(userModel.getName() + ", seja bem vindo(a)!");

        System.out.println("🔥 Enviando mensagem para fila: " + routingKey);

        try {
            rabbitTemplate.convertAndSend("", routingKey, emailDto);
            System.out.println("✅ Mensagem enviada!");
        } catch (Exception e) {
            System.out.println("❌ ERRO AO ENVIAR:");
            e.printStackTrace();
        }
    }
}
