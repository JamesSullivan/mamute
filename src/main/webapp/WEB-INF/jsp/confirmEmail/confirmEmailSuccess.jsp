<c:set var="title" value="${t['metas.signup.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title">${t['confirm_email.success']}</h2>
<a href="${linkTo[ListController].home}" >Click here to continue</a>
