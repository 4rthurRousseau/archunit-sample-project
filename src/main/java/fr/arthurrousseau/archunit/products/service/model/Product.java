package fr.arthurrousseau.archunit.products.service.model;

public record Product(String id, String label, double price, String sensitiveField) {
}
