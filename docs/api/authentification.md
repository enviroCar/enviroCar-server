---
layout: default
---

# Registration
See [`POST /rest/users`](Users)

### Login

Just send the following HTTP headers with every request to authenticate.

    X-User: username
    X-Token: token

### Logout

As the service is stateless (and a Facebook/Google integration is planned) there is no logout.
