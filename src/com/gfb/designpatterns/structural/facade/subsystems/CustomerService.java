package com.gfb.designpatterns.structural.facade.subsystems;

public class CustomerService {

    public record Customer(String id, String name, String email, String cep, String city) {}

    public Customer findCustomerById(String customerId) {
        System.out.printf("  [Subsistema Cliente] Localizando dados cadastrais para ID: %s...%n", customerId);
        // Simulação de busca em banco de dados
        return new Customer(customerId, "Dev Senior da Silva", "dev.senior@example.com", "01310-100", "São Paulo - SP");
    }

    public boolean validateCustomer(Customer customer) {
        return customer != null && customer.email() != null && customer.email().contains("@");
    }
}
