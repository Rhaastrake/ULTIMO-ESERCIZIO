<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Ospedale — Medici</title>
    <style>
        body    { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1      { color: #1a3c5c; }
        .nav a  { margin-right: 15px; color: #1565c0; text-decoration: none; font-weight: bold; }
        .filtri { margin: 15px 0; display: flex; gap: 10px; }
        .filtri a { padding: 6px 14px; background: #1565c0; color: white;
                    border-radius: 4px; text-decoration: none; font-size: 13px; }
        .filtri a:hover { background: #1a3c5c; }
        table   { width: 100%; border-collapse: collapse; background: white;
                  box-shadow: 0 2px 6px rgba(0,0,0,0.1); border-radius: 6px; overflow: hidden; }
        th      { background: #1565c0; color: white; padding: 12px 15px; text-align: left; }
        td      { padding: 10px 15px; border-bottom: 1px solid #e0e0e0; }
        tr:last-child td { border-bottom: none; }
        tr:hover td { background: #e3f2fd; }
        .successo { background: #bbdefb; border: 1px solid #1565c0;
                    padding: 10px; margin: 15px 0; border-radius: 4px; color: #0d47a1; }
        .info-bar { color: #555; margin-bottom: 12px; }
    </style>
</head>
<body>

<h1>🏥 Ospedale — Medici</h1>
<div class="nav">
    <a href="${pageContext.request.contextPath}/lista-visite">Lista visite</a>
    <a href="${pageContext.request.contextPath}/nuova-visita">Nuova Visita</a>
</div>

<c:if test="${param.messaggio == 'visita_creata'}">
    <div class="successo">✅ Visita registrata correttamente!</div>
</c:if>

<div class="filtri" style="margin-top:15px;">
    <a href="${pageContext.request.contextPath}/medici">Tutti</a>
    <a href="${pageContext.request.contextPath}/medici?specializzazione=cardiologia">Cardiologia</a>
    <a href="${pageContext.request.contextPath}/medici?specializzazione=ortopedia">Ortopedia</a>
    <a href="${pageContext.request.contextPath}/medici?specializzazione=neurologia">Neurologia</a>
    <a href="${pageContext.request.contextPath}/medici?specializzazione=pediatria">Pediatria</a>
</div>

<div class="info-bar"><strong>${filtro}</strong> — ${totale} medici trovati</div>

<table>
    <thead>
        <tr>
            <th>ID</th><th>Nome</th><th>Specializzazione</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="m" items="${medici}">
            <tr>
                <td>${m.id}</td>
                <td><strong>Dr. ${m.nome}</strong></td>
                <td>${m.specializzazione}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty medici}">
            <tr><td colspan="3" style="text-align:center;color:#999;">Nessun medico trovato.</td></tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
