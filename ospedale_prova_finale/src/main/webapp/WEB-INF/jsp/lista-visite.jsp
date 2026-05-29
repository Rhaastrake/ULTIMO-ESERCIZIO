<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Ospedale — Visite</title>
    <style>
        body     { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1       { color: #1a3c5c; }
        .nav a   { margin-right: 15px; color: #1565c0; text-decoration: none; font-weight: bold; }
        .nav a:hover { text-decoration: underline; }
        table    { width: 100%; border-collapse: collapse; background: white;
                   box-shadow: 0 2px 6px rgba(0,0,0,0.1); border-radius: 6px; overflow: hidden; }
        th       { background: #1565c0; color: white; padding: 12px 15px; text-align: left; }
        td       { padding: 10px 15px; border-bottom: 1px solid #e0e0e0;
                   vertical-align: top; }
        tr:last-child td { border-bottom: none; }
        tr:hover td      { background: #e3f2fd; }
        .info-bar { color: #555; margin: 15px 0 12px; }
        .badge   { padding: 3px 9px; border-radius: 10px; font-size: 12px;
                   background: #bbdefb; color: #0d47a1; }
        .descrizione { color: #444; font-size: 13px; max-width: 280px; }
        .empty   { text-align: center; color: #999; padding: 20px; }
    </style>
</head>
<body>

<h1>🏥 Ospedale — Tutte le Visite</h1>

<div class="nav">
    <a href="${pageContext.request.contextPath}/medici">Medici</a>
    <a href="${pageContext.request.contextPath}/nuova-visita">Nuova Visita</a>
</div>

<div class="info-bar"><strong>${totale}</strong> visita/e registrata/e</div>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Data</th>
            <th>Paziente</th>
            <th>Medico</th>
            <th>Specializzazione</th>
            <th>Descrizione</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="v" items="${visite}">
            <tr>
                <td>${v.id}</td>
                <td>${v.dataVisita}</td>
                <td>${v.paziente.nome}</td>
                <td><strong>Dr. ${v.medico.nome}</strong></td>
                <td><span class="badge">${v.medico.specializzazione}</span></td>
                <td class="descrizione">${v.descrizione}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty visite}">
            <tr>
                <td colspan="6" class="empty">Nessuna visita registrata.</td>
            </tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
