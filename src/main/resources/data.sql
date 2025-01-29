-- Initialisation des tables

INSERT INTO Personne(matricule, nom, prenom, poste) VALUES
(1, 'Dupont', 'Jean', 'Chef de projet'),
(2, 'Martin', 'Alice', 'Développeur'),
(3, 'Durand', 'Sophie', 'Analyste');

INSERT INTO Projet(code, nom, debut, fin) VALUES
(1, 'Projet Alpha', '2024-01-10', '2024-06-25'),
(2, 'Projet Beta', '2024-02-15', '2024-06-30');

INSERT INTO Participation(id, role, pourcentage, projet_id, personne_matricule) VALUES
(1, 'Leader', 50.0, 1, 1),
(2, 'Développeur', 30.0, 1, 2),
(3, 'Analyste', 20.0, 2, 3);

