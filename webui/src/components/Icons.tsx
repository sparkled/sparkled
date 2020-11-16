import React from 'react'

const Icons: Record<string, React.FC<{ fill?: string; size: number }>> = {
  More: props => {
    return (
      <svg xmlns='http://www.w3.org/2000/svg' height={props.size} viewBox='0 0 30 122.9'>
        <path
          fill={props.fill ?? '#fff'}
          d='M15 0a15 15 0 110 30 15 15 0 010-30zm0 93a15 15 0 110 30 15 15 0 010-30zm0-47a15 15 0 110 30 15 15 0 010-30z'
        />
      </svg>
    )
  },

  Plus: props => {
    return (
      <svg xmlns='http://www.w3.org/2000/svg' width={props.size} height={props.size} viewBox='0 0 1792 1792'>
        <path
          fill={props.fill ?? '#fff'}
          d='M1600 736v192q0 40-28 68t-68 28h-416v416q0 40-28 68t-68 28h-192q-40 0-68-28t-28-68v-416h-416q-40 0-68-28t-28-68v-192q0-40 28-68t68-28h416v-416q0-40 28-68t68-28h192q40 0 68 28t28 68v416h416q40 0 68 28t28 68z'
        />
      </svg>
    )
  },

  Slider: props => {
    return (
      <svg xmlns='http://www.w3.org/2000/svg' width={props.size} height={props.size} viewBox='0 0 1792 1792'>
        <path
          fill={props.fill ?? '#fff'}
          d='M480 1408v128H128v-128h352zm352-128q26 0 45 19t19 45v256q0 26-19 45t-45 19H576q-26 0-45-19t-19-45v-256q0-26 19-45t45-19h256zm160-384v128H128V896h864zM352 384v128H128V384h224zm1312 1024v128H928v-128h736zM704 256q26 0 45 19t19 45v256q0 26-19 45t-45 19H448q-26 0-45-19t-19-45V320q0-26 19-45t45-19h256zm640 512q26 0 45 19t19 45v256q0 26-19 45t-45 19h-256q-26 0-45-19t-19-45V832q0-26 19-45t45-19h256zm320 128v128h-224V896h224zm0-512v128H800V384h864z'
        />
      </svg>
    )
  },
}

export default Icons
