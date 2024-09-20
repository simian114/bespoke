function setCookie(name, value, days) {
    var date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000)); // days를 밀리초로 변환
    var expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function saveTopBanner(e) {
    // TODO: session storage 에 저장
    const ret = e.detail.xhr.response;
    sessionStorage.setItem('topBanner', ret);
}

function handleAfterOnLoad(e) {
    const path = e.detail.pathInfo.requestPath;
    if (path === "/banner/top-banner") {
        //
    } else {
        // 추가..
    }
}

function tinymceCodeinline(editor) {
    editor.ui.registry.addToggleButton('codeinline', {
        icon: 'sourcecode',
        tooltip: "Format as code",
        onAction: (_) => editor.execCommand('mceToggleFormat', false, 'code'),
        onSetup: (api) => {
            const changed = editor.formatter
                .formatChanged('code', (state) => api.setActive(state));
            return () => changed.unbind();
        }
    });
}

function initProfileIntroduceEditor() {
    tinymce.init({
        selector: 'textarea',
        plugins: 'searchreplace autolink autosave visualblocks image link media codesample table charmap pagebreak nonbreaking anchor insertdatetime lists wordcount help quickbars emoticons accordion',
        toolbar: "undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | align numlist bullist | link image | table media | lineheight outdent indent| forecolor backcolor removeformat | charmap emoticons |  pagebreak anchor codesample codeinline",
        menubar: 'edit view insert format table help',
        menu: {
            edit: { title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall | searchreplace' },
            view: { title: 'View', items: 'visualaid visualchars visualblocks' },
            insert: { title: 'Insert', items: 'image link media codesample inserttable | math | charmap emoticons hr | pagebreak nonbreaking anchor tableofcontents | insertdatetime' },
            format: { title: 'Format', items: 'bold italic underline strikethrough codeinline superscript subscript codeformat | styles blocks fontfamily fontsize align lineheight | forecolor backcolor | language | removeformat' },
            table: { title: 'Table', items: 'inserttable' },
            help: { title: 'Help', items: 'help' }
        },
        quickbars_insert_toolbar: '',
        quickbars_selection_toolbar: 'italic bold  | h2 h3 blockquote code codeinline',
        content_css: ['/css/application.css'],
        body_class: 'content',
        content_style: 'body { margin: 16px; }',
        image_caption: true,
        height: 600,
        resize: true,
        promotion: false,
        setup: function (editor) {
            editor.on('change', function () {
                tinymce.triggerSave();
            })
            tinymceCodeinline(editor)
        }
    });
}

function initPostTinyMceEditor(uploadUrl) {
    async function imagesUploadHandler(blobInfo, progress) {
        try {
            const xhr = new XMLHttpRequest();
            xhr.withCredentials = true;
            xhr.open('POST', uploadUrl);

            const uploadPromise = new Promise((resolve, reject) => {
                xhr.upload.onprogress = (e) => {
                    progress(e.loaded / e.total * 100);
                };

                xhr.onload = () => {
                    if (xhr.status === 403) {
                        reject({ message: 'HTTP Error: ' + xhr.status, remove: true });
                        return;
                    }

                    if (xhr.status < 200 || xhr.status >= 300) {
                        reject('HTTP Error: ' + xhr.status);
                        return;
                    }

                    const json = JSON.parse(xhr.responseText);

                    if (!json || !json.data || typeof json.data.url != 'string') {
                        reject('Invalid JSON: ' + xhr.responseText);
                        return;
                    }

                    resolve(json.data.url);
                };

                xhr.onerror = () => {
                    reject('Image upload failed due to a XHR Transport error. Code: ' + xhr.status);
                };
            });

            const formData = new FormData();
            formData.append('file', blobInfo.blob(), blobInfo.filename());

            xhr.send(formData);

            // xhr.send() 이후 uploadPromise가 완료될 때까지 대기
            const url = await uploadPromise;
            return url;

        } catch (error) {
            console.error('Image upload failed:', error);
            throw error;
        }
    }
    tinymce.init({
        selector: 'textarea',
        plugins: 'searchreplace autolink autosave visualblocks image link media codesample table charmap pagebreak nonbreaking anchor insertdatetime lists wordcount help quickbars emoticons accordion',
        toolbar: "undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | align numlist bullist | link image | table media | lineheight outdent indent| forecolor backcolor removeformat | charmap emoticons |  pagebreak anchor codesample codeinline",
        menubar: 'edit view insert format table help',
        menu: {
            edit: { title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall | searchreplace' },
            view: { title: 'View', items: 'visualaid visualchars visualblocks' },
            insert: { title: 'Insert', items: 'image link media codesample inserttable | math | charmap emoticons hr | pagebreak nonbreaking anchor tableofcontents | insertdatetime' },
            format: { title: 'Format', items: 'bold italic underline strikethrough codeinline superscript subscript codeformat | styles blocks fontfamily fontsize align lineheight | forecolor backcolor | language | removeformat' },
            table: { title: 'Table', items: 'inserttable' },
            help: { title: 'Help', items: 'help' }
        },
        content_css: ['/css/application.css'],
        body_class: 'content',
        content_style: 'body { margin: 16px; }',
        image_caption: true,
        height: 600,
        resize: true,
        promotion: false,
        id: "editor",
        quickbars_selection_toolbar: 'italic bold | h2 h3 blockquote codeinline',
        images_upload_handler: imagesUploadHandler,
        setup: function (editor) {
            editor.on('change', function () {
                tinymce.triggerSave();
            })
            tinymceCodeinline(editor)
        }
    });

}