{{- define "chartLabels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "appChart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | lower }}
{{- end -}}

{{- define "appName" -}}
{{ .Chart.Name }}
{{- end -}}

{{- define "appVersion" -}}
{{ .Chart.AppVersion }}
{{- end -}}

{{- define "volumes" -}}
  {{- range $k, $v := .Values.volumes -}}
    {{/* To define name of the volume */}}
    {{- printf "- name: %s-%s" (include "appName" $) $k | replace "-app" "" | nindent 2 -}}

    {{/* To determine a content of the volume (ConfigMap or Secret) and a name of this content */}}
    {{- if eq $v.type "config" -}}
      {{- printf "configMap:" | nindent 4 -}}
      {{- printf "name: %s-%s-%s" (include "appName" $) $k $v.type | replace "-app" "" | nindent 6 -}}
      {{- if $v.items -}}
        {{- printf "items:" | nindent 6 -}}
        {{- printf "- key: %s" $v.items.key | nindent 8 -}}
        {{- printf "path: %s" $v.items.path | nindent 10 -}}
      {{- end -}}
    {{- else -}}
      {{- printf "secret:" | nindent 4 -}}
      {{- printf "secretName: %s-%s-%s" (include "appName" $) $k $v.type | replace "-app" "" | nindent 6 -}}
    {{- end -}}
  {{- end -}}
{{- end -}}

{{- define "volumeMount" -}}
  {{- $ := index . 0 }}
  {{- $container := index . 2 }}
  {{- with index . 1 }}
    {{- range $k, $v := .Values.volumes }}
      {{- if index $v "mountPath" $container -}}
        {{- printf "- name: %s-%s" (include "appName" $) $k | replace "-app" "" | nindent 12 -}}
        {{- printf "mountPath: %s" (index $v "mountPath" $container) | nindent 14 -}}
        {{- if index $v "subPath" $container -}}
          {{- printf "subPath: %s" (index $v "subPath" $container) | nindent 14 -}}
        {{- end -}}
      {{- end -}}
    {{- end -}}
  {{- end -}}
{{- end -}}

