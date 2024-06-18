# archunit

### Commandes utiles

Récupération de la liste des produits
`curl -X GET localhost:8080/products`

Récupération d'un produit spécifique
`curl -X GET localhost:8080/products/{id}`

AJout d'un produit
`curl -X POST localhost:8080/products -d "{ \"label\": \"Awesome dev.to article !\", \"price\": 1000 }" -H "Content-Type: application/json"`