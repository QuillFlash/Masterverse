function getCsrfToken()
{
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    return tokenMeta ? tokenMeta.getAttribute('content') : null;
}

function getCsrfHeader()
{
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return headerMeta ? headerMeta.getAttribute('content') : null;
}

function confirmPostDeletion(post_id)
{
    console.log("Deleting post with ID:", post_id);
    if (confirm('Are you sure you want to delete this post? THIS CANNOT BE UNDONE!!!'))
    {
        const csrfToken = getCsrfToken();
        const csrfHeader = getCsrfHeader();
        const headers = {};
        if (csrfHeader && csrfToken)
        {
            headers[csrfHeader] = csrfToken;
        }
        fetch(`/delete_post/${post_id}`, {method: 'DELETE', headers: headers})
            .then(response =>
            {
                location.reload();
                return false
            })
        .catch(error =>
        {
            console.error('Error during deletion:', error);
        });
    }
}

function confirmCommentDeletion(comment_id)
{
    console.log("Deleting comment with ID:", comment_id);
    if (confirm('Are you sure you want to delete this comment? THIS CANNOT BE UNDONE!!!'))
    {
        const csrfToken = getCsrfToken();
        const csrfHeader = getCsrfHeader();
        const headers = {};
        if (csrfHeader && csrfToken)
        {
            headers[csrfHeader] = csrfToken;
        }
        fetch(`/delete_comment/${comment_id}`, {method: 'DELETE', headers: headers})
        .then(response =>
        {
            location.reload();
            return false
        })
        .catch(error =>
        {
            console.error('Error during deletion:', error);
        });
    }
}

function confirmAccountDeletion(account_id)
{
    if (confirm('Are you sure you want to delete your account? THIS CANNOT BE UNDONE!!!'))
    {
        const csrfToken = getCsrfToken();
        const csrfHeader = getCsrfHeader();
        const headers = {};
        if (csrfHeader && csrfToken)
        {
            headers[csrfHeader] = csrfToken;
        }
        fetch(`/delete_account/${account_id}`, {method: 'DELETE', headers: headers})
            .then(response =>
            {
                location.reload();
                return false
            })
        .catch(error =>
        {
            console.error('Error during deletion:', error);
        });
    }
}