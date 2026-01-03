# Convertisseur XML ↔ JSON - Implémentation Native

## 📋 Description

Ce projet contient des implémentations **100% natives Java** pour convertir entre XML et JSON, sans utiliser de bibliothèques externes. Seules les API standard du JDK sont utilisées.

## 🔧 Classes Natives

### 1. `XmlToJsonNatif.java`

**Conversion : XML → JSON**

**Bibliothèques JDK utilisées :**
- `javax.xml.parsers.DocumentBuilder` - Parser XML
- `org.w3c.dom.*` - Manipulation du DOM
- `java.util.LinkedHashMap` - Préserver l'ordre des éléments

**Fonctionnalités :**
- ✅ Parse le XML avec DOM Parser
- ✅ Convertit les attributs XML en propriétés JSON
- ✅ Gère les éléments multiples (créés en tant qu'arrays JSON)
- ✅ Préserve le contenu texte avec la clé `#text`
- ✅ Gère les éléments vides (null)
- ✅ Formate le JSON avec indentation (2 espaces)
- ✅ Échappe les caractères spéciaux JSON

**Exemple :**
```xml
<person age="30">
  <name>Jean</name>
</person>
```
→
```json
{
  "person": {
    "age": "30",
    "name": "Jean"
  }
}
```

### 2. `JsonToXmlNatif.java`

**Conversion : JSON → XML**

**Bibliothèques JDK utilisées :**
- `java.util.*` - Collections et parsing manuel
- Aucune bibliothèque JSON externe !

**Fonctionnalités :**
- ✅ Parse JSON manuellement (sans bibliothèque externe)
- ✅ Gère les objets, arrays, strings, nombres, booléens, null
- ✅ Convertit les propriétés simples en attributs XML
- ✅ Gère le contenu mixte avec `#text`
- ✅ Gère les arrays JSON (éléments XML répétés)
- ✅ Formate le XML avec indentation (2 espaces)
- ✅ Échappe les caractères spéciaux XML (&, <, >, ", ')

**Exemple :**
```json
{
  "person": {
    "age": "30",
    "name": "Jean"
  }
}
```
→
```xml
<?xml version="1.0" encoding="UTF-8"?>
<person age="30">
  <name>Jean</name>
</person>
```

## 🎯 Utilisation dans l'Interface JavaFX

### Mode de conversion

L'application propose deux modes :
1. **Mode API** : Utilise des bibliothèques externes (org.json, etc.)
2. **Mode Natif** : Utilise uniquement le code Java pur (implémentations ci-dessus)

### Basculer entre les modes

Dans le menu **Options** :
- ☑️ **Utiliser API** : Active les bibliothèques externes
- ☑️ **Utiliser Code Natif** : Active les implémentations natives

## 📂 Fichiers de test

Quatre fichiers de test sont fournis :

1. **`test.xml`** - Exemple complexe (bibliothèque de livres)
2. **`test.json`** - Version JSON du fichier ci-dessus
3. **`test_simple.xml`** - Exemple simple (personne)
4. **`test_simple.json`** - Version JSON du fichier ci-dessus

## 🚀 Comment tester

1. **Lancer l'application JavaFX**
2. **Choisir le mode** : Menu Options → Utiliser Code Natif
3. **Ouvrir un fichier de test** : Bouton "Open" → Sélectionner `test.xml` ou `test.json`
4. **Sélectionner la conversion** : XML TO JSON ou JSON to XML
5. **Convertir** : Bouton "Convert"
6. **Sauvegarder le résultat** : Bouton "Save"

## ⚙️ Architecture

```
src/main/java/com/example/transformers/
├── converter/
│   ├── api/
│   │   ├── XmlToJsonApi.java        (utilise org.json)
│   │   └── JsonToXmlApi.java        (utilise org.json)
│   └── natif/
│       ├── XmlToJsonNatif.java      ✅ 100% JDK natif
│       └── JsonToXmlNatif.java      ✅ 100% JDK natif
└── HelloController.java              (contrôleur JavaFX)
```

## 🔍 Détails techniques

### Gestion des attributs XML

**XML → JSON :**
```xml
<book id="1" title="Test"/>
```
→
```json
{
  "book": {
    "id": "1",
    "title": "Test"
  }
}
```

**JSON → XML :**
Les propriétés simples (non-objets, non-arrays) deviennent des attributs :
```json
{
  "book": {
    "id": "1",
    "title": "Test"
  }
}
```
→
```xml
<book id="1" title="Test"/>
```

### Gestion des éléments multiples

**XML → JSON :**
```xml
<root>
  <item>A</item>
  <item>B</item>
  <item>C</item>
</root>
```
→
```json
{
  "root": {
    "item": ["A", "B", "C"]
  }
}
```

### Gestion du contenu mixte

**XML avec contenu et attributs :**
```xml
<price currency="EUR">12.99</price>
```
→
```json
{
  "price": {
    "currency": "EUR",
    "#text": "12.99"
  }
}
```

## ✨ Avantages de l'implémentation native

- 🚀 **Pas de dépendances externes** : Pas de conflits de versions
- 🎯 **Contrôle total** : Personnalisable selon vos besoins
- 📦 **Léger** : Taille du JAR réduite
- 🔒 **Sécurité** : Moins de surface d'attaque
- 📚 **Pédagogique** : Comprendre les mécanismes internes

## ⚠️ Limitations connues

- Le parser JSON manuel ne gère pas les nombres scientifiques (1e10)
- Les namespaces XML ne sont pas préservés
- La validation de schéma n'est pas implémentée
- Performances inférieures aux bibliothèques optimisées pour de très gros fichiers

## 📝 Licence

Ce code est fourni à des fins éducatives et peut être modifié selon vos besoins.

